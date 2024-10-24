//mysql框架
const mysql = require('mysql')
const { db_user, db_password, db_database } = require('../config')
const config = require('../config')

//数据库连接对象
const db = mysql.createPool({
    host:config.db_host,
    user:db_user,
    password:db_password,
    database:db_database
})


module.exports=
{
    db
}