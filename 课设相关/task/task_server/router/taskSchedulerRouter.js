const express = require('express')
const router = express.Router()
const taskSchedulerHandler = require('../router_handler/taskSchedulerHandler')

//生成任意执行时间且有优先级的调度结果(优先级高的尽可能靠前)
router.post('/genWMResultVeryFirst',taskSchedulerHandler.genWMResultVeryFirst)

module.exports = router