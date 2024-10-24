#include "CGLCanvas.h"

CGLCanvas::CGLCanvas(HDC hDC)
{
    int nPixelFormat; //像素格式变量
    static PIXELFORMATDESCRIPTOR pfd;
    double dblFOV = 3.1415926 / 8;

    m_zNear = 0.125;
    m_zDistance = 1;
    m_zFar = 8;
    m_dblTanHalfFov = tan(dblFOV);
    m_dblRotateAngle = 0;
    m_lpdblRotateAxis[0] = 0;
    m_lpdblRotateAxis[1] = 0;
    m_lpdblRotateAxis[2] = 1;
    m_bFirstTime = true;
    m_iProjectionType = 0;
    m_bLButtonDown = false;

    memset(&pfd, 0, sizeof(pfd));
    pfd.nSize = sizeof(pfd);
    pfd.nVersion = 1;
    pfd.dwFlags = PFD_DRAW_TO_WINDOW | PFD_SUPPORT_OPENGL | PFD_DOUBLEBUFFER;
    pfd.iPixelType = PFD_TYPE_RGBA;
    pfd.cColorBits = 32;
    pfd.cDepthBits = 16;
    pfd.iLayerType = PFD_MAIN_PLANE;
    //    static PIXELFORMATDESCRIPTOR pfd  = { sizeof(PIXELFORMATDESCRIPTOR), //数据结构大小
    //        1, //版本号，总设为1
    //        PFD_DRAW_TO_WINDOW | //支持窗口
    //        PFD_SUPPORT_OPENGL | //支持OpenGL
    //        PFD_DOUBLEBUFFER, //支持双缓存
    //        PFD_TYPE_RGBA, //RGBA颜色模式
    //        32, //32位颜色模式
    //        0, 0, 0, 0, 0, 0, //忽略颜色为，不使用
    //        0, //无alpha缓存
    //        0, //忽略偏移位
    //        0, //无累积缓存
    //        0, 0, 0, 0, //忽略累积位
    //        16, //16位z-buffer（z缓存）大小
    //        0, //无模板缓存
    //        0, //无辅助缓存
    //        PFD_MAIN_PLANE, //主绘制平面
    //        0, //保留的数据项
    //        0, 0, 0 }; //忽略层面掩模
    //    //选择最匹配的像素格式，返回索引值
    nPixelFormat = ChoosePixelFormat(hDC, &pfd);
    //设置环境设备的像素格式
    SetPixelFormat(hDC, nPixelFormat, &pfd);
    hRC = wglCreateContext(hDC); //创建OpenGL绘图环境并创建一个指向OpenGL绘制环境的句柄
    wglMakeCurrent(hDC, hRC); //将传递过来的绘制环境设置为OpenGL将要进行绘制的当前绘制环境
    m_bDrawCube = false;
    m_bDrawOctagon = true;
}

void CGLCanvas::OnSize(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    m_iWidth = LOWORD(lParam);
    m_iHeight = HIWORD(lParam);
    m_iHalfWidth = m_iWidth / 2;
    m_iHalfHeight = m_iHeight / 2;
    glViewport(0, 0, m_iWidth, m_iHeight);         // 重置当前的视口glMatrixMode(GL_PROJECTION);
    m_fnObserve();
    m_fnCast();
    InvalidateRect(hwnd, NULL, TRUE);
}

void CGLCanvas::m_fnDrawWireFrameCube()
{

    int i;
    double lpCoordinates[] = \
    {\
        0.25, 0.25, -0.25, \
        - 0.25, 0.25, -0.25, \
        - 0.25, -0.25, -0.25, \
        0.25, -0.25, -0.25, \
        0.25, 0.25, 0.25, \
        - 0.25, 0.25, 0.25, \
        - 0.25, -0.25, 0.25, \
        0.25, -0.25, 0.25\
    };    //正六面体八个顶点的坐标
    int lpEdgeInd[] = \
    {\
        0, 1, 2, 3, 4, 5, 6, 7,\
        0, 3, 1, 2, 4, 7, 5, 6,\
        0, 4, 1, 5, 2, 6, 3, 7\
    };
    glBegin(GL_LINES);    //正六面体由12条棱构成
    for(i = 0; i< 12; ++i)    //绘制线框正六面体的过程
    {
        glColor3d(1.0, 1.0, 1.0);
        glVertex3d(lpCoordinates[lpEdgeInd[i * 2] * 3], lpCoordinates[lpEdgeInd[i * 2] * 3 + 1], lpCoordinates[lpEdgeInd[i * 2] * 3 + 2]);
        glVertex3d(lpCoordinates[lpEdgeInd[i * 2 + 1] * 3], lpCoordinates[lpEdgeInd[i * 2 + 1] * 3 + 1], lpCoordinates[lpEdgeInd[i * 2 + 1] * 3 + 2]);
    }
    glEnd();    //绘制结束
}


void CGLCanvas::m_fnDrawSolidCube()
{

    int i;
    double lpCoordinates[] = \
    {\
        0.125, 0.125, -0.125, \
        - 0.125, 0.125, -0.125, \
        - 0.125, -0.125, -0.125, \
        0.125, -0.125, -0.125, \
        0.125, 0.125, 0.125, \
        - 0.125, 0.125, 0.125, \
        - 0.125, -0.125, 0.125, \
        0.125, -0.125, 0.125\
    };    //正六面体八个顶点的坐标
    double lpColor[] = \
    {\
        1.0, 0.875, 0.875, \
        0.75, 1.0, 1.0, \
        0.875, 1.0, 1.0, \
        1.0, 0.75, 1.0, \
        0.875, 0.875, 1.0, \
        1.0, 1.0, 0.75\
    };    //正六面体六个面的颜色
    int lpFacetInd[] = \
    {\
        0, 4, 7, 3, \
        1, 2, 6, 5, \
        0, 1, 5, 4, \
        2, 3, 7, 6, \
        4, 5, 6, 7, \
        3, 2, 1, 0\
    };    //正六面体六个面对应的顶点索引
    glBegin(GL_QUADS);    //正六面体由6个四边形面构成
    for(i = 0; i< 6; ++i)    //绘制实体正六面体的过程
    {
        glColor3d(lpColor[i * 3], lpColor[i * 3 + 1], lpColor[i * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4] * 3], lpCoordinates[lpFacetInd[i * 4] * 3 + 1], lpCoordinates[lpFacetInd[i * 4] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 1] * 3], lpCoordinates[lpFacetInd[i * 4 + 1] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 1] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 2] * 3], lpCoordinates[lpFacetInd[i * 4 + 2] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 2] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 3] * 3], lpCoordinates[lpFacetInd[i * 4 + 3] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 3] * 3 + 2]);
    }
    glEnd();    //绘制结束
}


void CGLCanvas::m_fnDrawOctagon()
{
    int i;
    double lpCoordinates[] = \
    {\
        0.25, 0, 0, \
        -0.25, 0, 0, \
        0, 0.25, 0, \
        0, -0.25, 0, \
        0, 0, 0.25, \
        0, 0, -0.25\
    };
    double lpColor[] = \
    {
        1.0, 0.75, 0.75, \
        0.75, 1.0, 1.0, \
        0.75, 1.0, 0.75, \
        1.0, 0.75, 1.0, \
        0.75, 0.75, 1.0, \
        1.0, 1.0, 0.75
    };
    int lpFacetInd[] = \
    {
        0, 2, 4, \
        2, 1, 4, \
        1, 3, 4, \
        3, 0, 4, \
        2, 0, 5, \
        1, 2, 5, \
        3, 1, 5, \
        0, 3, 5\
    };
    glBegin(GL_TRIANGLES);
    for (i = 0; i < 8; ++i)
    {
        glColor3d(lpColor[lpFacetInd[i * 3] * 3], lpColor[lpFacetInd[i * 3] * 3 + 1], lpColor[lpFacetInd[i * 3] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 3] * 3], lpCoordinates[lpFacetInd[i * 3] * 3 + 1], lpCoordinates[lpFacetInd[i * 3] * 3 + 2]);
        glColor3d(lpColor[lpFacetInd[i * 3 + 1] * 3], lpColor[lpFacetInd[i * 3 + 1] * 3 + 1], lpColor[lpFacetInd[i * 3 + 1] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 3 + 1] * 3], lpCoordinates[lpFacetInd[i * 3 + 1] * 3 + 1], lpCoordinates[lpFacetInd[i * 3 + 1] * 3 + 2]);
        glColor3d(lpColor[lpFacetInd[i * 3 + 2] * 3], lpColor[lpFacetInd[i * 3 + 2] * 3 + 1], lpColor[lpFacetInd[i * 3 + 2] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 3 + 2] * 3], lpCoordinates[lpFacetInd[i * 3 + 2] * 3 + 1], lpCoordinates[lpFacetInd[i * 3 + 2] * 3 + 2]);
    }
    glEnd();
}

void CGLCanvas::OnPaint(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    PAINTSTRUCT PS;
    HDC hDC;
    //    SetCurrent(*m_context);
    //    wxPaintDC dc(this);
    if(m_bFirstTime)    //首次绘制必须填写相关的变换矩阵，并打开深度测试
    {
        m_bFirstTime = false;
        m_fnRotate();
        m_fnObserve();
        m_fnCast();
        glEnable(GL_DEPTH_TEST);
    }
    glClearColor(0.0, 0.0, 0.0, 0.0);    //背景颜色设为黑色
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    //清理场景和深度缓冲
    glLoadIdentity();    //把变换矩阵设为单位矩阵
    glPushMatrix();    //把单位矩阵压栈
    //OpenGL里规定矩阵入栈的顺序与变换的顺序相反
    glMultMatrixd(m_lpdblProjectTrans);
    glMultMatrixd(m_lpdblViewTrans);
    m_fnDrawWireFrameCube();
    glPushMatrix();
    glMultMatrixd(m_lpdblWorldTrans);
    if (m_bDrawCube)
    {
        m_fnDrawSolidCube();
    }
    if (m_bDrawOctagon)
    {
        m_fnDrawOctagon();
    }
    glPopMatrix();
    glPopMatrix();    //弹出栈内矩阵
    //把所绘制的图形输出到屏幕
    glFlush();
    hDC = BeginPaint(hwnd, &PS);
    SwapBuffers(hDC);
    EndPaint(hwnd, &PS);
    //ValidateRect(hwnd, NULL);
}


void CGLCanvas::m_fnRotate()    //填写旋转矩阵
{
    CGeoPack::fnFillRotateMatrix(m_lpdblWorldTrans, m_lpdblRotateAxis, m_dblRotateAngle);    //这里用的是罗德里格向量旋转公式填写的
}

void CGLCanvas::m_fnObserve()    //填写视口矩阵
{
    int i;
    memset(m_lpdblViewTrans, 0, 16 * sizeof(double));
    for(i = 0; i < 4; ++i)    //视口变换仅是平移变换，所以左上方三乘三的子矩阵为单位矩阵
    {
        m_lpdblViewTrans[i * 4 + i] = 1;
    }
    m_lpdblViewTrans[14] = -m_zDistance;    //填写平移向量
}

void CGLCanvas::m_fnCast()    //填写投影矩阵
{
    memset(m_lpdblProjectTrans, 0, 16 * sizeof(double));
    switch(m_iProjectionType)    //不同的投影模式下的投影矩阵填写
    {
    case 0:    //透视投影模式下的投影矩阵填写
    {
        m_lpdblProjectTrans[0] = double(m_iHalfHeight) / double(m_iHalfWidth);    //为了保证屏幕长宽比不为1的情况下，图形渲染不会变形，x要除以长宽比以保证投影坐标下正方形和长方形屏幕的正确映射
        m_lpdblProjectTrans[5] = 1;    //y齐次坐标在除以w之前的数值和原来相同
        m_lpdblProjectTrans[10] = -m_zFar * m_dblTanHalfFov / (m_zFar - m_zNear);    //z/w需要映射到0和1之间，其中近平面映射为0，远平面映射为1
        m_lpdblProjectTrans[11] = -m_dblTanHalfFov;    //w的值正比于z，以体现近大远小的效果。由于可视范围内的物体z坐标都是负的， 此项必须为负，才能保证w为正
        m_lpdblProjectTrans[14] = -m_zNear * m_zFar * m_dblTanHalfFov / (m_zFar - m_zNear);    //深度坐标的常数项，为了实现z/w到0和1之间的映射
    }
        break;
    case 1:    //平行投影只需要让z坐标变成正的，x坐标在满足长宽比不为1的映射下不变形即可
    {
        m_lpdblProjectTrans[0] = 2 * double(m_iHalfHeight) / double(m_iHalfWidth);    //为了保证屏幕长宽比不为1的情况下，图形渲染不会变形，x要除以长宽比以保证投影坐标下正方形和长方形屏幕的正确映射
        m_lpdblProjectTrans[5] = 2;
        m_lpdblProjectTrans[10] = -1;
        m_lpdblProjectTrans[15] = 1;
    }
        break;
    }
}

void CGLCanvas::OnKeyDown(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch(wParam)
    {
    case VK_UP:
    {
        m_zDistance /= 1.2;
        m_zFar /= 1.2;
        m_zNear /= 1.2;
        m_fnObserve();    //重新填写视口变换矩阵
        m_fnCast();    //重新填写投影变换矩阵
        InvalidateRect(hwnd, NULL, TRUE);
    }
        break;
    case VK_DOWN:
    {
        m_zDistance *= 1.2;
        m_zFar *= 1.2;
        m_zNear *= 1.2;
        m_fnObserve();    //重新填写视口变换矩阵
        m_fnCast();    //重新填写投影变换矩阵
        InvalidateRect(hwnd, NULL, TRUE);
    }
        break;
    case 'C':
    case 'c':
        m_bDrawCube = !m_bDrawCube;
        InvalidateRect(hwnd, NULL, TRUE);
        break;
    case 'O':
    case 'o':
        m_bDrawOctagon = !m_bDrawOctagon;
        InvalidateRect(hwnd, NULL, TRUE);
        break;
    case 'P':
    case 'p':
        m_iProjectionType = 1 - m_iProjectionType;
        m_fnCast();    //重新填写投影变换矩阵
        InvalidateRect(hwnd, NULL, TRUE);
        break;
    };
}

void CGLCanvas::OnLButtonDown(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    m_xMouse = LOWORD(lParam);
    m_yMouse = HIWORD(lParam);
    m_bLButtonDown = true;
}

void CGLCanvas::OnLButtonUp(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    m_bLButtonDown = false;
}


void CGLCanvas::OnMouseMove(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    int xMouse, yMouse;
    if(m_bLButtonDown)
    {
        xMouse = LOWORD(lParam);
        yMouse = HIWORD(lParam);
        int xOffset1 = m_xMouse - m_iHalfWidth;
        int yOffset1 = m_iHalfHeight - m_yMouse;
        int xOffset2 = xMouse - m_iHalfWidth;
        int yOffset2 = m_iHalfHeight - yMouse;
        //生成鼠标在屏幕上的位置对应的抽象球面的向量
        double lpdblVec1[3] = { double(xOffset1), double(yOffset1), sqrt(std::max<double>(0.0, double(m_iHalfHeight * m_iHalfHeight - xOffset1 * xOffset1 - yOffset1 * yOffset1))) };
        double lpdblVec2[3] = { double(xOffset2), double(yOffset2), sqrt(std::max<double>(0.0, double(m_iHalfHeight * m_iHalfHeight - xOffset2 * xOffset2 - yOffset2 * yOffset2))) };
        double lpdblCurrentAxis[3] = { 0, 0, 1 };    //鼠标拖动引起的旋转对应的旋转轴
        double dblCurrentRotateAngle = 0.0;    //鼠标拖动引起的旋转对应的旋转角度
        double lpdblNewAxis[3] = { 0, 0, 1 };    //鼠标拖动引起的旋转与原来旋转的合成对应的旋转轴
        double dblNewRotateAngle = 0.0;    //鼠标扡动引起的旋转与原来旋转的合成对应的旋转角度
        CGeoPack::fnMinRotation(lpdblCurrentAxis, dblCurrentRotateAngle, lpdblVec1, lpdblVec2);    //根据鼠标在抽象球面上的位置向量计算鼠标拖动引起的旋转
        CGeoPack::fnMergeRotation(lpdblNewAxis, dblNewRotateAngle, m_lpdblRotateAxis, m_dblRotateAngle, lpdblCurrentAxis, dblCurrentRotateAngle);    //鼠标拖动引起的旋转与原来旋转做乘法
        //下面几行把新合成的旋转设为用来画图的世界变换
        m_dblRotateAngle = dblNewRotateAngle;
        m_lpdblRotateAxis[0] = lpdblNewAxis[0];
        m_lpdblRotateAxis[1] = lpdblNewAxis[1];
        m_lpdblRotateAxis[2] = lpdblNewAxis[2];
        m_fnRotate();    //根据新的旋转变换的旋转轴和旋转角度填写旋转矩阵
        m_xMouse = xMouse;    //记录鼠标拖动后的新的坐标
        m_yMouse = yMouse;
        InvalidateRect(hwnd, NULL, TRUE);
    }
}
