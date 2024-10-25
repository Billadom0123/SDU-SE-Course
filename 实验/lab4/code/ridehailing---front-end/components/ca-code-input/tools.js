import { getCurrentInstance, ref, toRaw, watch, isProxy, isRef } from 'vue'
const inputTypes = ['input', 'modelValue']
// 数据双向绑定
export const useVModel = (pro = 'modelValue', vmCallback, proCallback) => {
    const { proxy } = getCurrentInstance()
    const singleValue = ref(proxy.$props[pro])
    const event = pro || 'modelValue'
    const options = {
        immediate: true,
        deep: true
    }
    useVModelBase(() => proxy.$props[pro], null, (...arg) => {
        singleValue.value = arg[0]
        proCallback?.(...arg)
    }, options)
    useVModelBase(singleValue, event, (...arg) => {
        vmCallback?.(...arg)
    }, options)
    return singleValue
}

export const useVModelBase = (source, event, callback, options = {
    deep: false,
    immediate: false
}) => {
    if (!source) return
    const emitEvent = useVMEvent()
    watch(source, (value, oldValue) => {
        emitEvent(event, value)
        callback && callback(value, oldValue)
    }, options)
}

export const useVMEvent = (vm) => {
    const _vm = getVM(vm)
    const _emit = _vm?.emit || _vm?.$emit?.bind(_vm)
    return (event, data) => {
        if (!event) return
        const newValue = getResponsiveValue(data)
        _emit(`update:${event}`, newValue)
    }
}

// 获取当前实例
function getVM(vm) {
    const _vm = vm || getCurrentInstance()
    if (!_vm) {
        throw Error('请在setup周期内调用这个方法，或传入当前实例')
    }
    return _vm
}

// 取响应式数据
export function getResponsiveValue(data) {
    const newData = getProxyData(data)
    if (getVarType(newData) === 'Object') {
        const resData = {}
        Object.keys(newData).forEach(key => {
            resData[key] = getProxyData(newData[key])
        })
        return resData
    }
    return newData
}

// 获取响应式数据
export function getProxyData(data) {
    return isRef(data) ? toRaw(data.value) : isProxy(data) ? toRaw(data) : data
}

// 判断数据类型
export function getVarType(anything = undefined) {
    return Object.prototype.toString.call(anything).slice(8, -1)
}

/**
 * 单位转换
 * value [String, Number] 要转换的值
 * unit [String] 默认单位
 * expUnit [String] 期望单位
*/
export function addUnit(value = 'auto', unit = 'rpx', expUnit = 'px') {
    if (!value) return `0${unit}`
    value = isNaN(value) ? value : `${value}${unit}`
    if (expUnit === 'px' && /upx|rpx/.test(value)) {
        // 所有数值（含单位）
        const strArr = value.replace(/\s+/g, ' ').split(' ')
        return strArr.map(o => {
            const matcher = o.match(/^(-?\d+)(.*)/)
            return `${/upx|rpx/.test(matcher[2]) ? uni.upx2px(matcher[1]) : matcher[1]}${expUnit}`
        }).join(' ')
    }
    return value
}

/**
 * @description: 通用的事件句柄
 * @param {ComponentInternalInstance} vm 当前实例
 * @return {Function} 触发emit方法
 */
export const useEmit = (vm) => {
    const _vm = getVM(vm)
    const _emit = _vm?.emit || _vm?.$emit?.bind(_vm)
    const vmEvent = useVMEvent()
    /**
   * @description:
   * @param {String} event emit事件句柄名称
   * @param {any} data 暴露出去的内容
   * @param {Boolean} format 是否要格式化进行转换
   * @return {Void}
   */
    return (event, data, format = true) => {
        if (!event) return
        const newValue = getResponsiveValue(data)
        if (inputTypes.includes(event)) {
            vmEvent(event, data)
        } else {
            _emit(event, format
                ? {
                    type: event,
                    detail: ['Array', 'Object'].includes(getVarType(newValue))
                        ? newValue
                        : {
                            value: newValue
                        }
                }
                : newValue)
        }
    }
}
