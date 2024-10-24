#include <windows.h>
#include <GL/gl.h>
#include "CGeoPack.hpp"
class CGLCanvas
{
public:
    double m_lpdblWorldTrans[16];    //��������任����
    double m_lpdblViewTrans[16];    //�ӿ�����任����
    double m_lpdblProjectTrans[16];    //ͶӰ����任����
    int m_iProjectionType;    //ͶӰ�任ģʽ��0Ϊ͸��ͶӰ��1Ϊƽ��ͶӰ
    int m_xMouse, m_yMouse;    //��갴��ȥʱ�����걣��
    int m_iHeight, m_iWidth;
    bool m_bLButtonDown;    //�Ƿ���괦�ڰ���ȥ��״̬
    void m_fnRotate();    //��д��ת����
    void m_fnObserve();    //��д�ӿھ���
    void m_fnCast();    //��дͶӰ����
    double m_zNear;    //��ƽ����۲��ߵľ���
    double m_zDistance;    //Զƽ����۲��ߵľ���
    double m_zFar;    //�۲���������ԭ��ľ���
    double m_dblRotateAngle;    //����任�е���ת�Ƕ�
    double m_lpdblRotateAxis[3];    //����任�е���ת��
    double m_dblTanHalfFov;    //��ֱ������Ұ�Ƕ�һ�������ֵ
    bool m_bFirstTime;    //��ǵ�һ������Ӧ�򿪵�OpenGL��Ⱦģʽ
    int m_iHalfWidth;    //��Ļ�İ���
    int m_iHalfHeight;    //��Ļ�İ�߶�
    HGLRC hRC;
    bool m_bDrawCube, m_bDrawOctagon;

    void OnKeyDown(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);
    void OnLButtonDown(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);
    void OnLButtonUp(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);
    void OnMouseMove(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);
    void OnSize(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);

private:
    void m_fnDrawWireFrameCube();
    void m_fnDrawOctagon();
    void m_fnDrawSolidCube();
public:
    void OnPaint(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam);
    CGLCanvas(HDC hDC);
};
