#include <iostream>
#define GLEW_STATIC
#include <GL/glew.h>
#include <GLFW/glfw3.h>
#include <string>
#include <cmath>
#include <Windows.h>
#include <Gl/glut.h> 
#define MAX_CHAR 128
#pragma comment( linker, "/subsystem:\"windows\" /entry:\"mainCRTStartup\"" )
using namespace std;

double angle = 0;
int direction = 1;
double speed = 0.5;
double Acceleration = 0.25;


void reverse()
{
    direction = -direction;
}

void speedUp()
{
    speed+=Acceleration;
}

void speedDown()
{
    
    if (speed >= Acceleration) {
        speed -= Acceleration;
    }
    else {
        speed = 0;
    }
    
}

void rotate()
{
    angle += direction*speed;
    if (angle > 360) angle = 0;
    glutPostRedisplay();
}

void printCharater(const char* str,float labelX,float labelY)
{
    glRasterPos2f(labelX, labelY);
    for (int i = 0; i < strlen(str); i++) glutBitmapCharacter(GLUT_BITMAP_8_BY_13, *(str + i));
}

struct button
{
    char* str = new char[100];
    bool cleck;
    void showButton(int r, int g, int b, const char* b_label,float labelX,float labelY,
        float top, float bottom, float left, float right) {

        glPushMatrix();

        if (cleck)
        {
            glScalef(0.98, 0.98, 1.0);
        }
        glColor3f(r, g, b);
        printCharater(b_label,labelX,labelY);
        glColor3f(1, 1, 1);
        glBegin(GL_QUADS);
        glVertex3f(left, top, 0);
        glVertex3f(right, top, 0);
        glVertex3f(right, bottom, 0);
        glVertex3f(left, bottom, 0);

        glEnd();


        glPopMatrix();
    }


};
button *reverseBtn,*speedUpBtn,*speedDownBtn;


void mouseFunc(GLint btn, GLint sta, int x, int y)
{
    int size = 700;
    int rxl = size/2-size*0.1, rxr=size/2+size*0.1, yb=size*0.05, yt=size*0.1,
        suxl=size*0.2,suxr=size*0.35,
        sdxl=size*0.65,sdxr=size*0.8;


    if (btn == GLUT_LEFT_BUTTON)
        switch (sta)
        {
        case GLUT_DOWN:
        {
            if (y >= yb && y <= yt) {
                if (x >= rxl && x <= rxr) {
                    reverse();
                    reverseBtn->cleck = true;
                }
                else if (x >= suxl && x <= suxr) {
                    speedUp();
                    speedUpBtn->cleck = true;
                }
                else if (x >= sdxl && x <= sdxr) {
                    speedDown();
                    speedDownBtn->cleck = true;
                }
            }
            break;
        }
        case GLUT_UP:
        {
            reverseBtn->cleck = false;
            speedUpBtn->cleck = false;
            speedDownBtn->cleck = false;
        }
        }
    glutPostRedisplay();
}

void keyBoard(unsigned char key, int x, int y)
{
    switch (key)
    {
    case 'r':reverse(); break;
    case 'u':speedUp(); break;
    case 'd':speedDown(); break;
    case 27:exit(0);
    }
}

void drawPinwheel()
{
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glPushMatrix();
    glRotatef(angle, 0, 0, 1);
    glScalef(1, 1, 1);
    //中轴
    glColor3f(1, 1, 0);
    glBegin(GL_TRIANGLES);
    glVertex3f(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, 0.1 * sqrt(3) - 0.2 * sqrt(3) / 6, 0);
    glEnd();

    //红扇叶
    glColor3f(1, 0, 0);
    glBegin(GL_TRIANGLES);
    glVertex3f(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1 * 27 / 52, -0.2 * 147 / 113 / 146 * 230 - 0.2 * sqrt(3) / 6, 0);
    glEnd();

    //蓝扇叶
    glRotatef(120, 0, 0, 1);
    glColor3f(0, 0, 1);
    glBegin(GL_TRIANGLES);
    glVertex3f(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1 * 27 / 52, -0.2 * 147 / 113 / 146 * 230 - 0.2 * sqrt(3) / 6, 0);
    glEnd();

    //绿扇叶
    glRotatef(120, 0, 0, 1);
    glColor3f(0, 1, 0);
    glBegin(GL_TRIANGLES);
    glVertex3f(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(-0.1f, -0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);

    glVertex3d(0.0f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(-0.2f, -0.2 * 147 / 113 - 0.2 * sqrt(3) / 6, 0);
    glVertex3d(0.1 * 27 / 52, -0.2 * 147 / 113 / 146 * 230 - 0.2 * sqrt(3) / 6, 0);
    glEnd();

    glPopMatrix();

    //风车把
    glScalef(1, 1, 1);
    glColor3f(1, 0, 1);
    glBegin(GL_POLYGON);
    glVertex3f(0.05, 0, 0.1);
    glVertex3f(-0.05, 0, 0.1);
    glVertex3f(-0.05, -0.7, 0.1);
    glVertex3f(0.05, -0.7, 0.1);
    glEnd();

    //按钮
    speedUpBtn->showButton(0,0,0,"Speed Up", -0.52,0.74,0.8, 0.7, -0.6, -0.3);
    reverseBtn->showButton(0, 0, 0, "Reverse",-0.06,0.74, 0.8, 0.7, -0.1, 0.1);
    speedDownBtn->showButton(0, 0, 0, "Speed Down", 0.36,0.74,0.8, 0.7, 0.3, 0.6);

    //按键提示
    printCharater("Speed Up->U", -0.54, 0.85);
    printCharater("Reverse->R", -0.06, 0.85);
    printCharater("Speed Down->D", 0.34, 0.85);


    glFlush();
    glutSwapBuffers();
}

void Init()
{
    glEnable(GL_DEPTH_TEST);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(0.77, 0.80, 0.84, 0.19);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(20, 1, 1, 50);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);
    glShadeModel(GL_SMOOTH);
    reverseBtn = new button;
    reverseBtn->cleck = false;
    speedUpBtn = new button;
    speedUpBtn->cleck = false;
    speedDownBtn = new button;
    speedDownBtn->cleck = false;
}

int main(int argc, char* argv[]) 
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(700, 700);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("Spinning Pinwheel");
    glEnable(GL_DEPTH_TEST | GL_LINE_SMOOTH | GL_POLYGON_SMOOTH | GL_POLYGON_STIPPLE);

    Init();
    glutDisplayFunc(&drawPinwheel);
    glutIdleFunc(&rotate);
    glutKeyboardUpFunc(&keyBoard);
    glutMouseFunc(&mouseFunc);
    glutMainLoop();

    return 0;
}
