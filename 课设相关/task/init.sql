-- 建库语句
CREATE SCHEMA `my_db_01` ;

--建表语句
CREATE TABLE `my_db_01`.`task_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
COMMENT = '202100300063李彦浩大二下数据结构与算法课程设计用户表';

