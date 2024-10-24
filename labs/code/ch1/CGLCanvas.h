#include <windows.h>
#include <GL/gl.h>
#include "CGeoPack.hpp"
class CGLCanvas
{
public:
    double m_lpdblWorldTrans[16];    //世界坐标变换矩阵
    double m_lpdblViewTrans[16];    //视口坐标变换矩阵
    double m_lpdblProjectTrans[16];    //投影坐标变换矩阵
    int m_iProjectionType;    //投影变换模式，0为透视投影，1为平行投影
    int m_xMouse, m_yMouse;    //鼠标按下去时的坐标保存
    int m_iHeight, m_iWidth;
    bool m_bLButtonDown;    //是否鼠标处于按下去的状态
    void m_fnRotate();    //填写旋转矩阵
    void m_fnObserve();    //填写视口矩阵
    void m_fnCast();    //填写投影矩阵
    double m_zNear;    //近平面离观察者的距离
    double m_zDistance;    //远平面离观察者的距离
    double m_zFar;    //观察者离坐标原点的距离
    double m_dblRotateAngle;    //世界变换中的旋转角度
    double m_lpdblRotateAxis[3];    //世界变换中的旋转轴
    double m_dblTanHalfFov;    //竖直方向视野角度一半的正切值
    bool m_bFirstTime;    //标记第一次运行应打开的OpenGL渲染模式
    int m_iHalfWidth;    //屏幕的半宽度
    int m_iHalfHeight;    //屏幕的半高度
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
