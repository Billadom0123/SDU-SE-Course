const express = require('express')
const router = express.Router()
const userHandler = require('../router_handler/userHandler')
const expressJoi = require('@escook/express-joi')
const {loginSchema} = require('../schema/user')

router.post('/register',expressJoi(loginSchema),userHandler.register)

router.post('/login',expressJoi(loginSchema),userHandler.login)

module.exports=router