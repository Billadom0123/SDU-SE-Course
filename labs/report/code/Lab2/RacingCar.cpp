#include <cmath>
#include <ctime>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
#include <iostream>
#include <GL/glew.h>
#include <GL/glut.h> 
#include <GLFW\glfw3.h>
using namespace std;

const double PI = 3.14159265359;
const float velocity_acceleration = 0.005f;
const float v_max = 0.05f;// max velocity
const float rad = PI / 180.0f;

float angle2Rad(float angle) {
	return rad * angle;
}

bool first_perspective = false; // user perspective
float translatex = 0;
float translatey = 0;
float cameraDistance = 40.0f;
float cameraAnglex = 0;
float cameraAngley = 120;


float v = 0; //velocity
float mx = 0;
float my = 0;
float turnAngle = 0;

float R = 0, G = 0.5, B = 0.5;

GLfloat lightPos[] = { 25.0, 25.0, 25.0, 1.0 };
GLfloat lookat[] = { 0.0, 0.0, 0.0 };
GLfloat up[] = { 0.0, 0.0, 1.0 };
GLdouble fovy = 60.0;
GLdouble nearPlane = 1.0;
GLdouble farPlane = 1000.0;

void keyboard(unsigned char key, int x, int y)
{
	switch (key) {
	case 't':
		first_perspective = !first_perspective;
		break;
	case ' ':
		v = 0;
		break;
	case 'w':
		v = v + velocity_acceleration;
		if (v > v_max) v = v_max;
		break;
	case 's':
		v = v - velocity_acceleration;
		if (v < -v_max / 2.0) v = -v_max / 2.0;
		break;
	case 'a':
		turnAngle += 10.0;
		break;
	case 'd':
		turnAngle -= 10.0;
		break;
	case 27:
		exit(0);
		break;
	default: {
		if (v > v_max) {
			v = v_max;
		}
		if (v < -v_max / 5.0) {
			v = -v_max / 5.0;
		}
	}
		   break;
	}
	glutPostRedisplay();
}

void draw_Facets(
	float RED,float GREEN,float BLUE,
	GLenum strategy,
	float cors[][3], int length
	) 
{
	glColor3f(RED,GREEN,BLUE);
	glBegin(strategy);
	for (int i = 0; i < length; i++) {
		glVertex3f(cors[i][0], cors[i][1], cors[i][2]);
	}
	glEnd();
}

void draw_track() {
	float height = 0.001f;
	//绘制地面
	glNormal3f(0, 0, 1);
	float corsGround[4][3] = {
		{-20.0f, -20.0f, 0},
		{20.0f, -20.0f, 0},
		{20.0f, 20.0f, 0},
		{-20.0f, 20.0f, 0}
	}; 
	draw_Facets(0, 0.6, 0.2, GL_QUADS,
			corsGround , 4);

	//跑道的直线部分：
	float corsLeftRide[4][3] = {
		{-10.0f, -8.0f, height},
		{-5.0f, -8.0f, height},
		{-5.0f, 8.0f, height},
		{-10.0f, 8.0f, height}
	};
	draw_Facets(1, 0.6, 0.2, GL_QUADS,
		corsLeftRide, 4);
	
	float corsRightRide[4][3] = {
		{10.0f, -8.0f, height},
		{10.0f, 8.0f, height},
		{5.0f, 8.0f, height},
		{5.0f, -8.0f, height}
	};
	draw_Facets(1, 0.6, 0.2, GL_QUADS,
		corsRightRide, 4);

	//跑道的圆环部分：
	float i;
	for (i = 0; i <= PI - 0.01; i += 0.01) {
		float corsUpperRing[4][3] = {
			{-5.0 * cos(i), 8 + 5.0 * sin(i), height},
			{-5.0 * cos(i + 0.01), 8 + 5.0 * sin(i + 0.01), height},
			{-10.0 * cos(i + 0.01), 8 + 10.0 * sin(i + 0.01), height},
			{-10.0 * cos(i), 8 + 10.0 * sin(i), height}
		};
		draw_Facets(1, 0.6, 0.2, GL_QUADS,
			corsUpperRing,4);
	}

	for (i = 0; i <= PI - 0.01; i += 0.01) {
		float corsLowerRing[4][3] = {
			{-5.0 * cos(i), -8 - 5.0 * sin(i), height},
			{-10.0 * cos(i), -8 - 10.0 * sin(i), height},
			{-10.0 * cos(i + 0.01), -8 - 10.0 * sin(i + 0.01), height},
			{-5.0 * cos(i + 0.01), -8 - 5.0 * sin(i + 0.01), height}
		};
		draw_Facets(1, 0.6, 0.2, GL_QUADS,
			corsLowerRing, 4);
	}
}

void draw_car() {
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	//车底面
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(-1.0f, 2.0f, 0.5f);
	glVertex3f(1.0f, 2.0f, 0.5f);
	glVertex3f(1.0f, -2.0f, 0.5f);
	glVertex3f(-1.0f, -2.0f, 0.5f);
	glEnd();
	//车头
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(-1.0f, 2.0f, 0.5f);
	glVertex3f(-1.0f, 2.0f, 1.5f);
	glVertex3f(1.0f, 2.0f, 1.5f);
	glVertex3f(1.0f, 2.0f, 0.5f);
	glEnd();
	//车尾
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(-1.0f, -2.0f, 1.5f);
	glVertex3f(-1.0f, -2.0f, 0.5f);
	glVertex3f(1.0f, -2.0f, 0.5f);
	glVertex3f(1.0f, -2.0f, 1.5f);
	glEnd();
	//左侧面
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(-1.0f, 2.0f, 1.5f);
	glVertex3f(-1.0f, 2.0f, 0.5f);
	glVertex3f(-1.0f, -2.0f, 0.5f);
	glVertex3f(-1.0f, -2.0f, 1.5f);
	glEnd();
	//右侧面
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(1.0f, 2.0f, 0.5f);
	glVertex3f(1.0f, 2.0f, 1.5f);
	glVertex3f(1.0f, -2.0f, 1.5f);
	glVertex3f(1.0f, -2.0f, 0.5f);
	glEnd();
	//车顶(从头向尾绘制，共有五个多边形拼接成)
	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(1.0f, 2.0f, 1.5f);
	glVertex3f(-1.0f, 2.0f, 1.5f);
	glVertex3f(-1.0f, 1.0f, 1.5f);
	glVertex3f(1.0f, 1.0f, 1.5f);
	glEnd();

	glBegin(GL_POLYGON);
	glColor3f(1, 1, 1);
	glVertex3f(-1.0f, 1.0f, 1.5f);
	glVertex3f(-1.0f, 0.5f, 2.5f);
	glVertex3f(1.0f, 0.5f, 2.5f);
	glVertex3f(1.0f, 1.0f, 1.5f);
	glEnd();

	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(1.0f, 0.5f, 2.5f);
	glVertex3f(-1.0f, 0.5f, 2.5f);
	glVertex3f(-1.0f, -1.0f, 2.5f);
	glVertex3f(1.0f, -1.0f, 2.5f);
	glEnd();

	glBegin(GL_POLYGON);
	glColor3f(1, 1, 1);
	glVertex3f(1.0f, -1.0f, 2.5f);
	glVertex3f(-1.0f, -1.0f, 2.5f);
	glVertex3f(-1.0f, -1.2f, 1.5f);
	glVertex3f(1.0f, -1.2f, 1.5f);
	glEnd();

	glBegin(GL_POLYGON);
	glColor3f(R, G, B);
	glVertex3f(1.0f, -1.2f, 1.5f);
	glVertex3f(-1.0f, -1.2f, 1.5f);
	glVertex3f(-1.0f, -2.0f, 1.5f);
	glVertex3f(1.0f, -2.0f, 1.5f);
	glEnd();
	//左车窗
	glBegin(GL_POLYGON);
	glColor3f(1, 1, 1);
	glVertex3f(-1.0f, 0.5f, 2.5f);
	glVertex3f(-1.0f, 1.0f, 1.5f);
	glVertex3f(-1.0f, -1.2f, 1.5f);
	glVertex3f(-1.0f, -1.2f, 2.5f);
	glEnd();
	//右车窗
	glBegin(GL_POLYGON);
	glColor3f(1, 1, 1);
	glVertex3f(1.0f, 1.0f, 1.5f);
	glVertex3f(1.0f, 0.5f, 2.5f);
	glVertex3f(1.0f, -1.2f, 2.5f);
	glVertex3f(1.0f, -1.2f, 1.5f);
	glEnd();

	//车轮	
	glColor3f(0, 0, 0);

	glTranslated(0.6f, 1.3f, 0.25);
	glRotatef(90, 0, 1, 0);
	glutSolidTorus(0.1, 0.25, 5, 100);

	glTranslated(-0.6f, 1.3f, 0.25);
	glRotatef(90, 0, 1, 0);
	glutSolidTorus(0.1, 0.25, 5, 100);

	glRotatef(-90, 0, 1, 0);
	glTranslated(0, -2.6f, 0);
	glRotatef(90, 0, 1, 0);
	glutSolidTorus(0.1, 0.25, 5, 100);

	glRotatef(-90, 0, 1, 0);
	glTranslated(1.2, 0, 0);
	glRotatef(90, 0, 1, 0);
	glutSolidTorus(0.1, 0.25, 5, 100);

	glPopMatrix();

}

void drawObjects(GLboolean shadowRender) {
	draw_track();
	glPushMatrix();
	glTranslatef(mx - 7.5, my, 0); //初始位置:(-7.5,0,0) 随mx,my位置变化
	glRotatef(turnAngle, 0, 0, 1);
	draw_car();
}


void display(void) {
	//计算物体的当前位移
	my += v * cos(angle2Rad(-turnAngle));
	mx += v * sin(angle2Rad(-turnAngle));
	//摄像机位置
	GLfloat z = 40.0f;
	GLfloat y = -20.0f;
	GLfloat x = 0.0f;
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPushMatrix();
	glLoadIdentity();
	if (first_perspective) {
		gluLookAt(mx - 7.5, my, 2.0,
			mx - 7.5 + sin(angle2Rad(-turnAngle)), my + cos(angle2Rad(-turnAngle)) , 2.0,
			up[0], up[1], up[2]);
	}
	else {
		gluLookAt(x, y, z,
			0,0,0,
			up[0], up[1], up[2]);
	}
	drawObjects(GL_FALSE);
	glPopMatrix();
	glutSwapBuffers();
}


void init(void) {
	GLfloat  white[] = { 1.0, 1.0, 1.0, 1.0 };
	glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
	glLightfv(GL_LIGHT0, GL_SPECULAR, white);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, white);
	glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHTING);
	glEnable(GL_TEXTURE_2D);
	glEnable(GL_COLOR_MATERIAL);
	glEnable(GL_CULL_FACE);
}

void reshape(int width, int height) {
	glViewport(0, 0, width, height);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fovy, (GLdouble)width / height, nearPlane, farPlane);
	glMatrixMode(GL_MODELVIEW);
}

int main(int argc, char** argv) {
	srand(time(0));
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(800, 800);
	glutInitWindowPosition(0, 0);
	glutCreateWindow(argv[0]);
	init();
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutIdleFunc(glutPostRedisplay);// do nothing
	glutMainLoop();
	return 0;
}
