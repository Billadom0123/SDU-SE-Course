#!/usr/bin/python3
# coding=utf8
# Date:2022/05/30import sys
import cv2
import time
import math
import rospy
import numpy as np
from threading import RLock, Timer, Thread

from std_srvs.srv import *
from std_msgs.msg import *
from sensor_msgs.msg import Image

from sensor.msg import Led
from chassis_control.msg import *
from visual_processing.msg import Result
from visual_processing.srv import SetParam
from intelligent_transport.srv import SetTarget
from hiwonder_servo_msgs.msg import MultiRawIdPosDur

from armpi_pro import PID
from armpi_pro import Misc
from armpi_pro import bus_servo_control
from kinematics import ik_transform
lock = RLock()
ik = ik_transform.ArmIK()

set_visual = 'tag'    # opposite makes the car move
detect_step = 'tracking' # current step for the car
stable = False        # whether can clamp
__isRunning = False
chassis_move = False  # whether is moving

# size of image
img_w = 640
img_h = 480
# color for LED
range_rgb = {
    'red': (0, 0, 255),
    'blue': (255, 0, 0),
    'green': (0, 255, 0),
}
color_list = { 0:'None', 1:'red', 2:'green', 3:'blue'}
target_color_index = 1

Arm_X = 500
Arm_Y = 100
arm_x = Arm_X
arm_y = Arm_Y
x_dis = 500 # 6th steering engine initial angle
y_dis = 0.15 # a magic number for ik solution
color_centreX = 320
color_centreY = 410
color_center_x = 0
color_center_y = 0

# PID Algorithm
# chassis tracking object
x_pid = PID.PID(P=0.70, I=0.0001, D=0.0001)
y_pid = PID.PID(P=1.30, I=0.0005, D=0.0001)
# arm tracking object
arm_x_pid = PID.PID(P=0.1, I=0.0001, D=0.0001)
arm_y_pid = PID.PID(P=0.18, I=0.0005, D=0.0001)
# cube tracking
color_x_pid = PID.PID(P=0.06, I=0, D=0)
color_y_pid = PID.PID(P=0.00003, I=0, D=0)

result_sub = None
heartbeat_timer = None

def run(msg):
    global lock
    global color_center_x,color_center_y
    global arm_x, arm_y
    global chassis_move

    data = int(msg.data)
    center_x = msg.center_x
    center_y = msg.center_y

    with lock:
        if __isRunning:
            if center_x > 0 and center_y > 0:
                # update where the cube is
                color_center_x = center_x
                color_center_y = center_y
                # arm tracking on axis X
                if abs(center_x - img_w / 2.0) < 15:
                    center_x = img_w / 2.0
                arm_x_pid.SetPoint = img_w / 2.0
                arm_x_pid.update(center_x)
                arm_x += arm_x_pid.output
                arm_x = 200 if arm_x < 200 else arm_x
                arm_x = 800 if arm_x > 800 else arm_x

                # arm tracking on axis Y
                if abs(center_y - img_h / 2.0) < 15:
                    center_y = img_h / 2.0
                arm_y_pid.SetPoint = img_h / 2.0
                arm_y_pid.update(center_y)
                arm_y += arm_y_pid.output
                arm_y = 50 if arm_y < 50 else arm_y
                arm_y = 300 if arm_y > 300 else arm_y

                bus_servo_control.set_servos(joints_pub, 20, ((3, arm_y), (6, arm_x)))

                # chassis tracking on axis X
                if abs(arm_x - Arm_X) < 5:
                    arm_x = Arm_X
                x_pid.SetPoint = Arm_X
                x_pid.update(arm_x)
                dx = x_pid.output
                dx = -200 if dx < -200 else dx
                dx = 200 if dx > 200 else dx

                # chassis tracking on axis Y
                if abs(arm_y - Arm_Y) < 5:
                    arm_y = Arm_Y
                y_pid.SetPoint = Arm_Y
                y_pid.update(arm_y)
                dy = -y_pid.output
                dy = -180 if dy < -180 else dy
                dy = 180 if dy > 180 else dy

                set_translation.publish(dx,dy)
                chassis_move = True
            else:
                # arrived
                if chassis_move:
                    chassis_move = False
                    stable = True
                    rospy.sleep(0.1)
                    set_translation.publish(0,0)

def move():
    global x_dis, y_dis
    global stable
    global set_visual, detect_step
    global chassis_move
    global color_list, target_color_index
    global color_center_x,color_center_y
    global __isRunning

    while __isRunning:
        if target_color_index == 0:
            __isRunning = False
            break
        if detect_step == 'tracking':
            # the car is near the cube and has stopped
            if not chassis_move:
                if set_visual == 'tag':
                    x_dis = 500
                    y_dis = 0.15
                    stable = False
                    set_visual = 'tracking'
                    # tracking current colored cube
                    visual_running('color', color_list[target_color_index])
                    target = ik.setPitchRanges((0, 0.15, 0.03), -180, -180, 0)
                    if target:
                        servo_data = target[1]
                        bus_servo_control.set_servos(joints_pub, 1500, ((1, 200), (2, 500), (3, servo_data['servo3']),
                                                                        (4, servo_data['servo4']),
                                                                        (5, servo_data['servo5']),
                                                                        (6, servo_data['servo6'])))
                        rospy.sleep(1.5)
                else:
                    diff_x = abs(color_center_x - color_centreX)
                    diff_y = abs(color_center_y - color_centreY)

                    if diff_x < 10:
                        color_x_pid.SetPoint = color_center_x
                    else:
                        color_x_pid.SetPoint = color_centreX
                    color_x_pid.update(color_center_x)
                    dx = color_x_pid.output
                    x_dis += int(dx)
                    x_dis = 200 if x_dis < 200 else x_dis
                    x_dis = 800 if x_dis > 800 else x_dis
                    # Y轴PID追踪
                    if diff_y < 10:
                        color_y_pid.SetPoint = color_center_y
                    else:
                        color_y_pid.SetPoint = color_centreY
                    color_y_pid.update(color_center_y)
                    dy = color_y_pid.output
                    y_dis += dy
                    y_dis = 0.12 if y_dis < 0.12 else y_dis
                    y_dis = 0.28 if y_dis > 0.28 else y_dis
                    # move the arm directly above the block
                    target = ik.setPitchRanges((0, round(y_dis, 4), 0.03), -180, -180, 0)
                    if target:
                        servo_data = target[1]
                        bus_servo_control.set_servos(joints_pub, 20, ((3, servo_data['servo3']),
                                                                      (4, servo_data['servo4']),
                                                                      (5, servo_data['servo5']), (6, x_dis)))

                    if dx < 2 and dy < 0.003 and not stable:  # until stable
                        num += 1
                        if num == 10:
                            stable = True  # ok to grab
                            num = 0
                    else:
                        num = 0

                    if stable:  # grab
                        offset_y = Misc.map(target[2], -180, -150, -0.03, 0.03)
                        set_rgb(color_list[target_color_index])  # set rgb LED
                        buzzer_pub.publish(0.1)  # buzzer

                        bus_servo_control.set_servos(joints_pub, 500, ((1, 120),))  # open claw
                        rospy.sleep(0.5)
                        target = ik.setPitchRanges((0, round(y_dis + offset_y, 5), -0.07), -180, -180, 0)  # arm reaches down
                        if target:
                            servo_data = target[1]
                            bus_servo_control.set_servos(joints_pub, 1000, ((3, servo_data['servo3']),
                                                                            (4, servo_data['servo4']),
                                                                            (5, servo_data['servo5']), (6, x_dis)))
                        rospy.sleep(1.5)
                        bus_servo_control.set_servos(joints_pub, 500, ((1, 500),))  # 闭合机械爪
                        rospy.sleep(0.8)

                        bus_servo_control.set_servos(joints_pub, 1500, (
                        (1, 500), (2, 500), (3, 80), (4, 825), (5, 625), (6, 500)))  # 机械臂抬起来
                        rospy.sleep(1.5)

                        stable = False # no cube on this position anymore
                        # back to tag
                        detect_step = 'tag'
            else:
                rospy.sleep(0.1)
        elif detect_step == 'tag':
            if not chassis_move:
                if set_visual == 'tracking':
                    x_dis = 500
                    y_dis = 0.15
                    stable = False
                    set_visual = 'tag'
                    visual_running('apriltag','')
                else:
                    diff_x = abs(color_center_x - color_centreX)
                    diff_y = abs(color_center_y - color_centreY)

                    if diff_x < 10:
                        color_x_pid.SetPoint = color_center_x
                    else:
                        color_x_pid.SetPoint = color_centreX
                    color_x_pid.update(color_center_x)
                    dx = color_x_pid.output
                    x_dis += int(dx)
                    x_dis = 200 if x_dis < 200 else x_dis
                    x_dis = 800 if x_dis > 800 else x_dis
                    # Y轴PID追踪
                    if diff_y < 10:
                        color_y_pid.SetPoint = color_center_y
                    else:
                        color_y_pid.SetPoint = color_centreY
                    color_y_pid.update(color_center_y)
                    dy = color_y_pid.output
                    y_dis += dy
                    y_dis = 0.12 if y_dis < 0.12 else y_dis
                    y_dis = 0.28 if y_dis > 0.28 else y_dis
                    # move the arm directly above the block
                    target = ik.setPitchRanges((0, round(y_dis, 4), 0.01), -180, -180, 0)
                    if target:
                        servo_data = target[1]
                        bus_servo_control.set_servos(joints_pub, 20, ((3, servo_data['servo3']),
                                                                      (4, servo_data['servo4']),
                                                                      (5, servo_data['servo5']), (6, x_dis)))

                    bus_servo_control.set_servos(joints_pub, 500, ((1, 150),))  # open claw
                    rospy.sleep(0.8)

                    # arm reset
                    bus_servo_control.set_servos(joints_pub, 1500, ((1, 75), (2, 500), (3, 80), (4, 825), (5, 625)))
                    rospy.sleep(1.5)
                    bus_servo_control.set_servos(joints_pub, 1500, ((6, 500),))
                    rospy.sleep(1.5)
                    detect_step = 'tracking'
                    target_color_index = (target_color_index+1) % len(color_list)  # next color
            else:
                rospy.sleep(0.1)

def init():
    rospy.loginfo("intelligent transport Init")
    initMove()
    reset()

def initMove(delay=True):
    with lock:
        bus_servo_control.set_servos(joints_pub, 1500, ((1, 75), (2, 500), (3, 80), (4, 825), (5, 625), (6, 500)))
    if delay:
        rospy.sleep(2)

def reset():
    global x_dis,y_dis
    global stable
    global set_visual,detect_step
    global chassis_move
    global color_center_x,color_center_y
    global x_pid,y_pid,arm_x_pid,arm_y_pid,color_x_pid,color_y_pid
    global arm_x, arm_y
    global target_color_index

    with lock:
        x_pid.clear()
        y_pid.clear()
        arm_x_pid.clear()
        arm_y_pid.clear()
        color_x_pid.clear()
        color_y_pid.clear()
        off_rgb()
        set_visual = 'tag'
        detect_step = 'tracking'
        stable = False
        chassis_move = False
        x_dis = 500
        y_dis = 0.15
        color_center_x = 0
        color_center_y = 0
        set_velocity.publish(0, 90, 0)
        arm_x = Arm_X
        arm_y = Arm_Y
        target_color_index = 1

def off_rgb():
    led = Led()
    led.index = 0
    led.rgb.r = 0
    led.rgb.g = 0
    led.rgb.b = 0
    rgb_pub.publish(led)
    led.index = 1
    rgb_pub.publish(led)

def set_rgb(color):
    global lock
    with lock:
        led = Led()
        led.index = 0
        led.rgb.r = range_rgb[color][2]
        led.rgb.g = range_rgb[color][1]
        led.rgb.b = range_rgb[color][0]
        rgb_pub.publish(led)
        rospy.sleep(0.05)
        led.index = 1
        rgb_pub.publish(led)
        rospy.sleep(0.05)

def set_running(msg):
    if msg.data:
        start_running()
    else:
        stop_running()

    return [True, 'set_running']


def stop_running():
    global lock
    global __isRunning

    rospy.loginfo("stop running intelligent transport")
    with lock:
        reset()
        __isRunning = False
        initMove(delay=False)
        set_velocity.publish(0, 0, 0)
        rospy.ServiceProxy('/visual_processing/set_running', SetParam)()

def start_running():
    global lock
    global __isRunning

    rospy.loginfo("start running Lab2")
    with lock:
        init()
        __isRunning = True
        rospy.sleep(0.1)
        # 运行子线程
        th = Thread(target=move)
        th.setDaemon(True)
        th.start()

# enter service
def enter_func(msg):
    global lock
    global result_sub

    rospy.loginfo("enter object tracking")
    init()
    with lock:
        if result_sub is None:
            # wake up visual_processing_node
            rospy.ServiceProxy('/visual_processing/enter', Trigger)()
            # subscribe the result from the detection module of visual_processing_node
            result_sub = rospy.Subscriber('/visual_processing/result', Result, run)

    return [True, 'enter']

# exit service
def exit_func(msg):
    global lock
    global result_sub
    global __isRunning
    global heartbeat_timer

    rospy.loginfo("exit Lab2")
    with lock:
        __isRunning = False
        rospy.ServiceProxy('/visual_processing/exit', Trigger)()
        reset()
        try:
            if result_sub is not None:
                result_sub.unregister()
                result_sub = None
            if heartbeat_timer is not None:
                heartbeat_timer.cancel()
                heartbeat_timer = None
        except BaseException as e:
            rospy.loginfo('%s', e)

    return [True, 'exit']

# heartbeat connection service
def heartbeat_srv_cb(msg):
    global heartbeat_timer

    if isinstance(heartbeat_timer, Timer):
        heartbeat_timer.cancel()
    if msg.data:
        heartbeat_timer = Timer(5, rospy.ServiceProxy('/Lab2/exit', Trigger))
        heartbeat_timer.start()
    rsp = SetBoolResponse()
    rsp.success = msg.data

    return rsp


if __name__ == '__main__':
    # init node
    rospy.init_node('Lab2', log_level=rospy.DEBUG)
    # Steering Engine
    joints_pub = rospy.Publisher('/servo_controllers/port_id_1/multi_id_pos_dur', MultiRawIdPosDur, queue_size=1)
    # ServiceProxy
    visual_running = rospy.ServiceProxy('/visual_processing/set_running', SetParam)
    # Services
    enter_srv = rospy.Service('/Lab2/enter', Trigger, enter_func)
    running_srv = rospy.Service('/Lab2/set_running', SetBool, set_running)
    set_target_srv = rospy.Service('/Lab2/set_target', SetTarget, set_target)
    exit_srv = rospy.Service('/Lab2/exit', Trigger, exit_func)
    heartbeat_srv = rospy.Service('/Lab2/heartbeat', SetBool, heartbeat_srv_cb)
    # Publishers
    # Chassis Control
    set_velocity = rospy.Publisher('/chassis_control/set_velocity', SetVelocity, queue_size=1)
    set_translation = rospy.Publisher('/chassis_control/set_translation', SetTranslation, queue_size=1)
    # Buzzer Control
    buzzer_pub = rospy.Publisher('/sensor/buzzer', Float32, queue_size=1)
    # RGB LED Control
    rgb_pub = rospy.Publisher('/sensor/rgb_led', Led, queue_size=1)
    rospy.sleep(0.5)  # pub之后必须延时才能生效

    try:
        rospy.spin()
    except KeyboardInterrupt:
        rospy.loginfo("Shutting down")
