<template>
    <view class="ca-code-input">
        <view class="ca-code-input-hd">
            <view :style="[inputStyle]">
                <!-- input组件若不想弹出软键盘，可设置为disabled -->
                <input
                    :value="codeValue" class="ca-code-input__input" :type="inputType" password :focus="inputFocus"
                    :disabled="disabled" :cursor="cursorIndex" :maxlength="maxlength" @input="handleInput" @blur="handleBlur">
            </view>
        </view>
        <view class="ca-code-input-bd">
            <view
                v-for="num in numArr" :key="num" class="ca-code-input__num" :style="[numStyle1, numStyle2(num)]"
                @click="handleNumClick(num)">
                <template v-if="num === '.'">
                    <text :style="[dottedStyle]" class="is-dotted" />
                </template>
                <template v-if="splitNum[num]">
                    <text v-if="dotted" :style="[dottedStyle]" class="is-dotted" />
                    <text v-else :style="[textStyle]">
                        {{ splitNum[num] }}
                    </text>
                </template>
                <!-- 下划线 -->
                <text v-if="border === 'bottom'" class="ca-code-input__line" :style="[lineStyle(num)]" />
                <!-- 闪烁框 -->
                <text
                    v-if="animation === 'twinkle' && inputFocus && cursorIndex === num"
                    class="ca-code-input__twinkle" :style="[twinkleStyle]" />
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useVModel, useEmit, addUnit } from './tools'
import codeProps from './props'
const props = defineProps(codeProps)
// 是否手动隐藏
const isHandHide = ref(false)
const maxLen = String(props.modelValue).length
const cursor = maxLen > props.maxlength ? props.maxlength : maxLen
const inputFocus = ref(props.focus)
const cursorIndex = ref(cursor)
// 注册事件句柄
const emitEvent = useEmit()
// 双向绑定
const codeValue = useVModel()
// 键盘类型
const inputType = computed(() => {
    return props.mode === 'code' ? 'number' : 'text'
})
// 验证码隐藏，防止用户界面看到复制的标识
const inputStyle = computed(() => {
    const fontSize = addUnit(props.fontSize, 'rpx', 'px')
    const transformX = `${parseInt(fontSize) * (props.maxlength + 1)}px`
    return {
        fontSize,
        transform: `translateX(-${transformX})`,
        width: `calc(100% + ${transformX})`,
        height: '0px',
        overflow: 'hidden'
    }
})
// 拆分codeValue进行num填充
const splitNum = computed(() => {
    const splitNum = codeValue.value.split('')
    if (props.fillText === null) {
        return splitNum.map(o => o.replace(/[0-9]/g, '●'))
    }
    return splitNum
})
// 输入框数组，必须转换成数组，部分Hbuilder版本和支付宝循环会从0开始，我们统一同数组代替差异
const numArr = computed(() => {
    const arr = generateArray(0, props.maxlength - 1)
    if (props.mode === 'car') {
        arr.splice(2, 0, '.')
    }
    return arr
})
// 输入框样式1
const numStyle1 = computed(() => {
    const size = addUnit(props.size)
    return {
        width: size,
        height: size,
        color: props.color
    }
})
// 输入框边框样式2
const numStyle2 = computed(() => {
    return (index) => {
        const space = addUnit(props.space)
        const hasSpace = parseInt(space) > 0
        const numStyle = {
            marginRight: hasSpace && index !== props.maxlength - 1 ? space : 0
        }
        const borderRadius = addUnit(props.radius)
        if (props.border === 'around') {
            numStyle.border = `${props.bold ? '1px' : '0.5px'} solid ${codeValue.length > index ? props.borderActiveColor : props.borderColor}`
            // 输入之间有间隙
            if (hasSpace) {
                numStyle.borderRadius = borderRadius
            } else {
                if (index !== props.maxlength - 1) {
                    numStyle.borderRight = 'none'
                }
                if (index === 0) {
                    numStyle.borderTopLeftRadius = borderRadius
                    numStyle.borderBottomLeftRadius = borderRadius
                }
                if (index === props.maxlength - 1) {
                    numStyle.borderTopRightRadius = borderRadius
                    numStyle.borderBottomRightRadius = borderRadius
                }
            }
            if (index === cursorIndex.value && props.animation === 'shine') {
                numStyle.boxShadow = `0 0 12rpx 0 ${props.animationColor}`
            }
        }
        if (index === '.') {
            numStyle.border = 'none'
            numStyle.width = `calc(${numStyle.width} / 3)`
        }
        return numStyle
    }
})
// 输入内容样式
const textStyle = computed(() => {
    return {
        fontSize: addUnit(props.fontSize),
        fontWeight: props.bold ? 'bold' : 'normal'
    }
})
// 下边框样式
const lineStyle = computed(() => {
    return (index) => {
        return {
            backgroundColor: codeValue.length > index ? props.borderActiveColor : props.borderColor,
            borderRadius: addUnit(props.radius),
            height: props.bold ? '8rpx' : '4rpx'
        }
    }
})
// 闪烁的样式
const twinkleStyle = computed(() => {
    return {
        color: props.animationColor,
        height: addUnit(props.fontSize)
    }
})
// 填充字符样式
const dottedStyle = computed(() => {
    const size = `${parseInt(addUnit(props.fontSize)) / (props.bold ? 2 : 3)}rpx`
    return {
        color: props.color,
        width: size,
        height: size
    }
})
// 验证码输入框点击
const handleNumClick = (value) => {
    if (props.disabled) { return }
    const cueLen = codeValue.value.length
    const cursor = Math.min(value, cueLen)
    changeCursorIndex(cursor, true)
}
// 生成数组
function generateArray(start, end) {
    return Array.from(new Array(end + 1).keys()).slice(start)
}
// 输入框输入
function handleInput(e) {
    const { value } = e.detail
    if (value === codeValue.value) { return }
    let cursor = cursorIndex.value
    if (value.length > codeValue.value.length) {
        cursor += 1
    } else {
        cursor -= 1
    }
    codeValue.value = value
    changeCursorIndex(cursor, false)
    if (codeValue.value.length >= props.maxlength) {
        handleSubmit()
    }
}
// 提交
function handleSubmit() {
    emitEvent('submit', codeValue.value.substr(0, props.maxlength))
}
// 改变游标位置
function changeCursorIndex(index, isBlur) {
    cursorIndex.value = Math.min(index, codeValue.value ? codeValue.value.length : 0)
    if (inputFocus.value && isBlur) {
        inputFocus.value = false
        // 标记一下,区分手动隐藏
        isHandHide.value = true
    } else {
        inputFocus.value = true
    }
}
function handleBlur() {
    inputFocus.value = false
    if (isHandHide.value) {
        setTimeout(() => {
            inputFocus.value = true
            isHandHide.value = false
        })
    }
}
</script>

<style scoped>
.ca-code-input {
    position: relative;
    display: inline-flex;
    flex-direction: row;
    box-sizing: border-box;
    vertical-align: middle;
}

.ca-code-input-hd {
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    overflow: hidden;
    opacity: 0;
    z-index: 1;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
}

.ca-code-input__input {
    height: 0px;
    padding: 0;
    color: transparent;
    caret-color: transparent;
    font-size: 24rpx;
    user-select: none;
    pointer-events: none;
    opacity: 0;
}

.ca-code-input-bd {
    display: flex;
    flex-direction: row;
    z-index: 2;
    position: relative;
}

.ca-code-input__num {
    display: flex;
    flex-direction: row;
    position: relative;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
}

.ca-code-input__num .is-dotted {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    border-radius: 100%;
    background-color: currentColor;
}

.ca-code-input__line {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
}

.ca-code-input__twinkle {
    animation: twinkle 1s infinite;
    width: 4rpx;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    background-color: currentColor;
}

@keyframes twinkle {
    from {
        background-color: currentColor;
    }

    to {
        background-color: transparent;
    }
}
</style>
