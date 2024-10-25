export default {
    name: {
        type: String,
        default: ''
    },
    // 是否禁止输入
    disabled: {
        type: Boolean,
        default: false
    },
    // 双向绑定的值
    modelValue: {
        type: String,
        default: ''
    },
    // 输入框模式，code - 验证码输入框  car - 车牌号输入框
    mode: {
        type: String,
        default: 'code'
    },
    // 边框类型, around-四周边框，bottom-底部边框，none-无边框
    border: {
        type: String,
        default: 'around'
    },
    // 输入字符个数
    maxlength: {
        type: Number,
        default: 4
    },
    // 使用远点填充，这边没办法自定义字符，因为特殊符号无法解决居中问题，这边扩展插槽自行处理
    dotted: {
        type: Boolean,
        default: false
    },
    // 是否自动获取焦点
    focus: {
        type: Boolean,
        default: false
    },
    // 字体颜色
    color: {
        type: String,
        default: '#333'
    },
    // 字体大小
    fontSize: {
        type: [String, Number],
        default: 32
    },
    // 输入框的大小，宽等于高
    size: {
        type: [String, Number],
        default: 70
    },
    // 字体和输入横线是否加粗
    bold: {
        type: Boolean,
        default: true
    },
    // 边框和线条颜色
    borderColor: {
        type: String,
        default: '#c8c7cc'
    },
    // 边框和线条颜色
    borderActiveColor: {
        type: String,
        default: '#c8c7cc'
    },
    // 字符间的距离
    space: {
        type: [String, Number],
        default: 8
    },
    // 边框圆角
    radius: {
        type: [String, Number],
        default: 8
    },
    // 动画类型 twinkle - 闪烁   shine - 发光(仅在border为around时生效)  none - 无动画
    animation: {
        type: String,
        default: 'twinkle'
    },
    animationColor: {
        type: String,
        default: '#007aff'
    },
    // 仅当systemKeyboard为false时失效，输入框会显示在键盘上
    linkage: {
        type: Boolean,
        default: true
    }
}
