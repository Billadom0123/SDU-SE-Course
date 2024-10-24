#include "CGLCanvas.h"

CGLCanvas::CGLCanvas(HDC hDC)
{
    int nPixelFormat; //���ظ�ʽ����
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
    //    static PIXELFORMATDESCRIPTOR pfd  = { sizeof(PIXELFORMATDESCRIPTOR), //���ݽṹ��С
    //        1, //�汾�ţ�����Ϊ1
    //        PFD_DRAW_TO_WINDOW | //֧�ִ���
    //        PFD_SUPPORT_OPENGL | //֧��OpenGL
    //        PFD_DOUBLEBUFFER, //֧��˫����
    //        PFD_TYPE_RGBA, //RGBA��ɫģʽ
    //        32, //32λ��ɫģʽ
    //        0, 0, 0, 0, 0, 0, //������ɫΪ����ʹ��
    //        0, //��alpha����
    //        0, //����ƫ��λ
    //        0, //���ۻ�����
    //        0, 0, 0, 0, //�����ۻ�λ
    //        16, //16λz-buffer��z���棩��С
    //        0, //��ģ�建��
    //        0, //�޸�������
    //        PFD_MAIN_PLANE, //������ƽ��
    //        0, //������������
    //        0, 0, 0 }; //���Բ�����ģ
    //    //ѡ����ƥ������ظ�ʽ����������ֵ
    nPixelFormat = ChoosePixelFormat(hDC, &pfd);
    //���û����豸�����ظ�ʽ
    SetPixelFormat(hDC, nPixelFormat, &pfd);
    hRC = wglCreateContext(hDC); //����OpenGL��ͼ����������һ��ָ��OpenGL���ƻ����ľ��
    wglMakeCurrent(hDC, hRC); //�����ݹ����Ļ��ƻ�������ΪOpenGL��Ҫ���л��Ƶĵ�ǰ���ƻ���
    m_bDrawCube = false;
    m_bDrawOctagon = true;
}

void CGLCanvas::OnSize(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    m_iWidth = LOWORD(lParam);
    m_iHeight = HIWORD(lParam);
    m_iHalfWidth = m_iWidth / 2;
    m_iHalfHeight = m_iHeight / 2;
    glViewport(0, 0, m_iWidth, m_iHeight);         // ���õ�ǰ���ӿ�glMatrixMode(GL_PROJECTION);
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
    };    //��������˸����������
    int lpEdgeInd[] = \
    {\
        0, 1, 2, 3, 4, 5, 6, 7,\
        0, 3, 1, 2, 4, 7, 5, 6,\
        0, 4, 1, 5, 2, 6, 3, 7\
    };
    glBegin(GL_LINES);    //����������12���⹹��
    for(i = 0; i< 12; ++i)    //�����߿���������Ĺ���
    {
        glColor3d(1.0, 1.0, 1.0);
        glVertex3d(lpCoordinates[lpEdgeInd[i * 2] * 3], lpCoordinates[lpEdgeInd[i * 2] * 3 + 1], lpCoordinates[lpEdgeInd[i * 2] * 3 + 2]);
        glVertex3d(lpCoordinates[lpEdgeInd[i * 2 + 1] * 3], lpCoordinates[lpEdgeInd[i * 2 + 1] * 3 + 1], lpCoordinates[lpEdgeInd[i * 2 + 1] * 3 + 2]);
    }
    glEnd();    //���ƽ���
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
    };    //��������˸����������
    double lpColor[] = \
    {\
        1.0, 0.875, 0.875, \
        0.75, 1.0, 1.0, \
        0.875, 1.0, 1.0, \
        1.0, 0.75, 1.0, \
        0.875, 0.875, 1.0, \
        1.0, 1.0, 0.75\
    };    //�����������������ɫ
    int lpFacetInd[] = \
    {\
        0, 4, 7, 3, \
        1, 2, 6, 5, \
        0, 1, 5, 4, \
        2, 3, 7, 6, \
        4, 5, 6, 7, \
        3, 2, 1, 0\
    };    //���������������Ӧ�Ķ�������
    glBegin(GL_QUADS);    //����������6���ı����湹��
    for(i = 0; i< 6; ++i)    //����ʵ����������Ĺ���
    {
        glColor3d(lpColor[i * 3], lpColor[i * 3 + 1], lpColor[i * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4] * 3], lpCoordinates[lpFacetInd[i * 4] * 3 + 1], lpCoordinates[lpFacetInd[i * 4] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 1] * 3], lpCoordinates[lpFacetInd[i * 4 + 1] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 1] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 2] * 3], lpCoordinates[lpFacetInd[i * 4 + 2] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 2] * 3 + 2]);
        glVertex3d(lpCoordinates[lpFacetInd[i * 4 + 3] * 3], lpCoordinates[lpFacetInd[i * 4 + 3] * 3 + 1], lpCoordinates[lpFacetInd[i * 4 + 3] * 3 + 2]);
    }
    glEnd();    //���ƽ���
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
    if(m_bFirstTime)    //�״λ��Ʊ�����д��صı任���󣬲�����Ȳ���
    {
        m_bFirstTime = false;
        m_fnRotate();
        m_fnObserve();
        m_fnCast();
        glEnable(GL_DEPTH_TEST);
    }
    glClearColor(0.0, 0.0, 0.0, 0.0);    //������ɫ��Ϊ��ɫ
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);    //����������Ȼ���
    glLoadIdentity();    //�ѱ任������Ϊ��λ����
    glPushMatrix();    //�ѵ�λ����ѹջ
    //OpenGL��涨������ջ��˳����任��˳���෴
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
    glPopMatrix();    //����ջ�ھ���
    //�������Ƶ�ͼ���������Ļ
    glFlush();
    hDC = BeginPaint(hwnd, &PS);
    SwapBuffers(hDC);
    EndPaint(hwnd, &PS);
    //ValidateRect(hwnd, NULL);
}


void CGLCanvas::m_fnRotate()    //��д��ת����
{
    CGeoPack::fnFillRotateMatrix(m_lpdblWorldTrans, m_lpdblRotateAxis, m_dblRotateAngle);    //�����õ����޵����������ת��ʽ��д��
}

void CGLCanvas::m_fnObserve()    //��д�ӿھ���
{
    int i;
    memset(m_lpdblViewTrans, 0, 16 * sizeof(double));
    for(i = 0; i < 4; ++i)    //�ӿڱ任����ƽ�Ʊ任���������Ϸ����������Ӿ���Ϊ��λ����
    {
        m_lpdblViewTrans[i * 4 + i] = 1;
    }
    m_lpdblViewTrans[14] = -m_zDistance;    //��дƽ������
}

void CGLCanvas::m_fnCast()    //��дͶӰ����
{
    memset(m_lpdblProjectTrans, 0, 16 * sizeof(double));
    switch(m_iProjectionType)    //��ͬ��ͶӰģʽ�µ�ͶӰ������д
    {
    case 0:    //͸��ͶӰģʽ�µ�ͶӰ������д
    {
        m_lpdblProjectTrans[0] = double(m_iHalfHeight) / double(m_iHalfWidth);    //Ϊ�˱�֤��Ļ����Ȳ�Ϊ1������£�ͼ����Ⱦ������Σ�xҪ���Գ�����Ա�֤ͶӰ�����������κͳ�������Ļ����ȷӳ��
        m_lpdblProjectTrans[5] = 1;    //y��������ڳ���w֮ǰ����ֵ��ԭ����ͬ
        m_lpdblProjectTrans[10] = -m_zFar * m_dblTanHalfFov / (m_zFar - m_zNear);    //z/w��Ҫӳ�䵽0��1֮�䣬���н�ƽ��ӳ��Ϊ0��Զƽ��ӳ��Ϊ1
        m_lpdblProjectTrans[11] = -m_dblTanHalfFov;    //w��ֵ������z�������ֽ���ԶС��Ч�������ڿ��ӷ�Χ�ڵ�����z���궼�Ǹ��ģ� �������Ϊ�������ܱ�֤wΪ��
        m_lpdblProjectTrans[14] = -m_zNear * m_zFar * m_dblTanHalfFov / (m_zFar - m_zNear);    //�������ĳ����Ϊ��ʵ��z/w��0��1֮���ӳ��
    }
        break;
    case 1:    //ƽ��ͶӰֻ��Ҫ��z���������ģ�x���������㳤��Ȳ�Ϊ1��ӳ���²����μ���
    {
        m_lpdblProjectTrans[0] = 2 * double(m_iHalfHeight) / double(m_iHalfWidth);    //Ϊ�˱�֤��Ļ����Ȳ�Ϊ1������£�ͼ����Ⱦ������Σ�xҪ���Գ�����Ա�֤ͶӰ�����������κͳ�������Ļ����ȷӳ��
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
        m_fnObserve();    //������д�ӿڱ任����
        m_fnCast();    //������дͶӰ�任����
        InvalidateRect(hwnd, NULL, TRUE);
    }
        break;
    case VK_DOWN:
    {
        m_zDistance *= 1.2;
        m_zFar *= 1.2;
        m_zNear *= 1.2;
        m_fnObserve();    //������д�ӿڱ任����
        m_fnCast();    //������дͶӰ�任����
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
        m_fnCast();    //������дͶӰ�任����
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
        //�����������Ļ�ϵ�λ�ö�Ӧ�ĳ������������
        double lpdblVec1[3] = { double(xOffset1), double(yOffset1), sqrt(std::max<double>(0.0, double(m_iHalfHeight * m_iHalfHeight - xOffset1 * xOffset1 - yOffset1 * yOffset1))) };
        double lpdblVec2[3] = { double(xOffset2), double(yOffset2), sqrt(std::max<double>(0.0, double(m_iHalfHeight * m_iHalfHeight - xOffset2 * xOffset2 - yOffset2 * yOffset2))) };
        double lpdblCurrentAxis[3] = { 0, 0, 1 };    //����϶��������ת��Ӧ����ת��
        double dblCurrentRotateAngle = 0.0;    //����϶��������ת��Ӧ����ת�Ƕ�
        double lpdblNewAxis[3] = { 0, 0, 1 };    //����϶��������ת��ԭ����ת�ĺϳɶ�Ӧ����ת��
        double dblNewRotateAngle = 0.0;    //���L���������ת��ԭ����ת�ĺϳɶ�Ӧ����ת�Ƕ�
        CGeoPack::fnMinRotation(lpdblCurrentAxis, dblCurrentRotateAngle, lpdblVec1, lpdblVec2);    //��������ڳ��������ϵ�λ��������������϶��������ת
        CGeoPack::fnMergeRotation(lpdblNewAxis, dblNewRotateAngle, m_lpdblRotateAxis, m_dblRotateAngle, lpdblCurrentAxis, dblCurrentRotateAngle);    //����϶��������ת��ԭ����ת���˷�
        //���漸�а��ºϳɵ���ת��Ϊ������ͼ������任
        m_dblRotateAngle = dblNewRotateAngle;
        m_lpdblRotateAxis[0] = lpdblNewAxis[0];
        m_lpdblRotateAxis[1] = lpdblNewAxis[1];
        m_lpdblRotateAxis[2] = lpdblNewAxis[2];
        m_fnRotate();    //�����µ���ת�任����ת�����ת�Ƕ���д��ת����
        m_xMouse = xMouse;    //��¼����϶�����µ�����
        m_yMouse = yMouse;
        InvalidateRect(hwnd, NULL, TRUE);
    }
}
