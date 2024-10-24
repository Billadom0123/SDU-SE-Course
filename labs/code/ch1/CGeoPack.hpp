#ifndef CGEOPACK_HPP
#define CGEOPACK_HPP
//#include <boost/math/quaternion.hpp>
#include <cmath>
#include <algorithm>
namespace CGeoPack
{
    template <typename T>
    static void fnCross(T *lp_tCross, T*lp_tVec1, T *lp_tVec2)
    {
        lp_tCross[0] = lp_tVec1[1] * lp_tVec2[2] - lp_tVec1[2] * lp_tVec2[1];
        lp_tCross[1] = lp_tVec1[2] * lp_tVec2[0] - lp_tVec1[0] * lp_tVec2[2];
        lp_tCross[2] = lp_tVec1[0] * lp_tVec2[1] - lp_tVec1[1] * lp_tVec2[0];
    }
    template <typename T>
    static T fnDot(T *lp_tVec1, T *lp_tVec2)
    {
        T _tDot = lp_tVec1[0] * lp_tVec2[0] + lp_tVec1[1] * lp_tVec2[1] + lp_tVec1[2] * lp_tVec2[2];
        return _tDot;
    }
    template <typename T>
    static void fnMinRotation(T *lp_tAxis, T &_tAngle, T *lp_tVec1, T *lp_tVec2)
    {
        fnCross<T>(lp_tAxis, lp_tVec1, lp_tVec2);
        T fAxisLen = sqrt(fnDot<T>(lp_tAxis, lp_tAxis));
        if(fAxisLen == 0)
        {
            lp_tAxis[0] = (T)0.0;
            lp_tAxis[1] = (T)0.0;
            lp_tAxis[2] = (T)1.0;
            _tAngle = (T)0.0;
        }
        else
        {
            lp_tAxis[0] /= fAxisLen;
            lp_tAxis[1] /= fAxisLen;
            lp_tAxis[2] /= fAxisLen;
            _tAngle = atan2(fAxisLen, fnDot<T>(lp_tVec1, lp_tVec2));
        }
    }
    template<typename T>
    static void fnMergeRotation(T *lp_tAxis, T &_tAngle, T *lp_tAxis1, T _tAngle1, T *lp_tAxis2, T _tAngle2)
    {
        int i;
        T _tHA1, _tHA2, _tS1, _tS2, _tC1, _tC2, _tCS, _tSC, _tCC, _tSS, _tAD, lp_tAC[3], _tSHA, _tCHA;
        _tHA1 = _tAngle1 / 2;
        _tHA2 = _tAngle2 / 2;
        _tC1 = cos(_tHA1);
        _tS1 = sin(_tHA1);
        _tC2 = cos(_tHA2);
        _tS2 = sin(_tHA2);
        _tCS = _tC1 * _tS2;
        _tSC = _tS1 * _tC2;
        _tCC = _tC1 * _tC2;
        _tSS = _tS1 * _tS2;
        _tAD = fnDot(lp_tAxis1, lp_tAxis2);
        fnCross(lp_tAC, lp_tAxis2, lp_tAxis1);
        for(i = 0; i < 3; ++i)
        {
            lp_tAxis[i] = lp_tAxis2[i] * _tCS + lp_tAxis1[i] * _tSC + lp_tAC[i] * _tSS;
        }
        _tCHA = _tCC - _tSS * _tAD;
        _tSHA = sqrt(fnDot(lp_tAxis, lp_tAxis));
		if (_tSHA != 0)
		{
			for(i = 0; i < 3; ++i)
			{
				lp_tAxis[i] /= _tSHA;
			}
			_tAngle = 2 * atan2(_tSHA, _tCHA);
		}
		else
		{
			lp_tAxis[0] = 0;
			lp_tAxis[1] = 0;
			lp_tAxis[2] = 1;
			_tAngle = 0;
		}
    }
    template<typename T>
    static void fnFillRotateMatrix(T *lp_tMatrix, T *lp_tAxis, T _tAngle)
    {
        int i, j;
        memset(lp_tMatrix, 0, 16 * sizeof(T));
        T lp_tOmega[3] = {lp_tAxis[0], lp_tAxis[1], lp_tAxis[2]};
        T _tAxisLen = sqrt(fnDot<T>(lp_tAxis, lp_tAxis));
        lp_tOmega[0] /= _tAxisLen;
        lp_tOmega[1] /= _tAxisLen;
        lp_tOmega[2] /= _tAxisLen;
        T _tCosAngle = cos(_tAngle);
        T _tVsAngle = 1 - _tCosAngle;
        T _tSinAngle = sin(_tAngle);
        memset(lp_tMatrix, 0, 16 * sizeof(T));
        for(i = 0; i < 3; ++i)
        {
            lp_tMatrix[i * 4 + i] += _tCosAngle;
            lp_tMatrix[i * 4 + (i + 2)%3] -= lp_tAxis[(i + 1)%3] * _tSinAngle;
            lp_tMatrix[i * 4 + (i + 1)%3] += lp_tAxis[(i + 2)%3] * _tSinAngle;
        }
        for(i = 0; i < 3; ++i)
        {
            for(j = 0; j < 3; ++j)
            {
                lp_tMatrix[i * 4 + j] += lp_tAxis[i] * lp_tAxis[j] * _tVsAngle;
            }
        }
        lp_tMatrix[15] = T(1.0);
    }
    template<typename T>
    static void fnSingleTransform(T *lp_tV2, T *lp_tV1, T *lp_tMatrix)
    {
        for(int j = 0; j < 4; ++j)
        {
            lp_tV2[j] = 0;
            for(int i = 0; i < 4; ++i)
            {
                lp_tV2[j] += lp_tMatrix[i * 4 + j] * lp_tV1[i];
            }
        }
    }
    template<typename T>
    static void fnViewTrans(T *lp_tViewCoord, T *lp_tVertex, T *lp_tWorldMat, T *lp_tViewMat, T * lp_tProjMat)
    {
        T lp_tBuffer1[4] = {lp_tVertex[0], lp_tVertex[1], lp_tVertex[2], 1};
        T lp_tBuffer2[4] = {0, 0, 0, 0};
        fnSingleTransform(lp_tBuffer2, lp_tBuffer1, lp_tWorldMat);
        fnSingleTransform(lp_tBuffer1, lp_tBuffer2, lp_tViewMat);
        fnSingleTransform(lp_tBuffer2, lp_tBuffer1, lp_tProjMat);
        lp_tBuffer1[0] = lp_tBuffer2[0]/lp_tBuffer2[3];
        lp_tBuffer1[1] = lp_tBuffer2[1]/lp_tBuffer2[3];
        lp_tBuffer1[2] = lp_tBuffer2[2]/lp_tBuffer2[3];
        lp_tBuffer1[3] = 1;
        lp_tViewCoord[0] = lp_tBuffer1[0];
        lp_tViewCoord[1] = lp_tBuffer1[1];
        lp_tViewCoord[2] = lp_tBuffer1[2];
    }
    template<typename T>
    static bool fnTestCoverage(T *lp_tCoord, T *lp_tCoord1, T *lp_tCoord2, T *lp_tCoord3, T *lp_tNorm)
    {
        T bCovered;
        T lp_tV1[3] = {lp_tCoord1[0] - lp_tCoord[0], lp_tCoord1[1] - lp_tCoord[1], lp_tCoord1[2] - lp_tCoord[2]};
        T lp_tV2[3] = {lp_tCoord2[0] - lp_tCoord[0], lp_tCoord2[1] - lp_tCoord[1], lp_tCoord2[2] - lp_tCoord[2]};
        T lp_tV3[3] = {lp_tCoord3[0] - lp_tCoord[0], lp_tCoord3[1] - lp_tCoord[1], lp_tCoord3[2] - lp_tCoord[2]};
        T fA = lp_tNorm[2];
        T fA1 = lp_tV2[0] * lp_tV3[1] - lp_tV3[0] * lp_tV2[1];
        T fA2 = lp_tV3[0] * lp_tV1[1] - lp_tV1[0] * lp_tV3[1];
        T fA3 = lp_tV1[0] * lp_tV2[1] - lp_tV2[0] * lp_tV1[1];
        if(fA > 0)
        {
            if(fA1 > 0 && fA2 > 0 && fA3 > 0)
            {
                if(fnDot(lp_tNorm, lp_tV1) < 0)
                {
                    bCovered = true;
                }
                else
                {
                    bCovered = false;
                }
            }
            else
            {
                bCovered = false;
            }
        }
        else if(fA < 0)
        {
            if(fA1 < 0 && fA2 < 0 && fA3 < 0)
            {
                if(fnDot(lp_tNorm, lp_tV1) > 0)
                {
                    bCovered = true;
                }
                else
                {
                    bCovered = false;
                }
            }
            else
            {
                bCovered = false;
            }
        }
        else
        {
            bCovered = false;
        }
        return bCovered;
    }
    template<typename T>
    static T fnZDistance(T *lp_tCoord, T *lp_tCoord1, T *lp_tCoord2, T *lp_tCoord3)
    {
        T _tZDistance;
        T lp_tV1[3] = {lp_tCoord1[0] - lp_tCoord[0], lp_tCoord1[1] - lp_tCoord[1], lp_tCoord1[2] - lp_tCoord[2]};
        T lp_tV2[3] = {lp_tCoord2[0] - lp_tCoord[0], lp_tCoord2[1] - lp_tCoord[1], lp_tCoord2[2] - lp_tCoord[2]};
        T lp_tV3[3] = {lp_tCoord3[0] - lp_tCoord[0], lp_tCoord3[1] - lp_tCoord[1], lp_tCoord3[2] - lp_tCoord[2]};
        T _tA12 = lp_tV1[0] * lp_tV2[1] - lp_tV1[1] * lp_tV2[0];
        T _tA23 = lp_tV2[0] * lp_tV3[1] - lp_tV2[1] * lp_tV3[0];
        T _tA31 = lp_tV3[0] * lp_tV1[1] - lp_tV3[1] * lp_tV1[0];
        T _tA123 = _tA12 + _tA23 + _tA31;
        _tZDistance = (lp_tV3[2] * _tA12 + lp_tV1[2] * _tA23 + lp_tV2[2] * _tA31) / _tA123;
        return _tZDistance;
    }
};


#endif // CGEOPACK_H
