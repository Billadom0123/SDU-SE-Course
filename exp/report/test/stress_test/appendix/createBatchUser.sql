DROP PROCEDURE IF EXISTS cutomer_user;
DELIMITER ;;
CREATE PROCEDURE customer_user (IN counter INT)
BEGIN
	DECLARE i,userId,customerId,phone	BIGINT;
    set i=1;
    set userId = 1688734750117007361;
    set customerId = 1687839914740494338;
    set phone = 17349742869;
    WHILE i<=counter DO
		INSERT INTO user(id,name,phone,password,credit,role_id,deleted,version) Values (userId+i,CONCAT('user',i),CONCAT(phone+i,''),'fcea920f7412b5da7be0cf42b8c93759',1500,1,0,1);
        INSERT INTO customer(id,carpool_rate,remain,user_id,deleted,version) VALUES (customerId+i,0,0,userId+i,0,1);
        SET i=i+1;
	END WHILE ;
END ;;
customer

DROP PROCEDURE IF EXISTS driver_user;
DELIMITER ;;
CREATE PROCEDURE driver_user (IN counter INT)
BEGIN
	DECLARE i,j,userId,driverId,phone	BIGINT;
    set i=1;
    set j=i+1000;
    set userId = 1688734750117008361;
    set driverId = 1688734750427385857;
    set phone = 13372100020;
    WHILE i<=counter DO
		INSERT INTO user(id,name,phone,password,credit,role_id,deleted,version) Values (userId+i,CONCAT('user',i+1000),CONCAT(phone+i,''),'fcea920f7412b5da7be0cf42b8c93759',1500,2,0,1);
		INSERT INTO driver(id,comment_rate,income,user_id,deleted,version,license) VALUES (driverId+i,0,0,userId+i,0,1,'C1');
        
        SET i=i+1;
        set j=i+1000;
	END WHILE ;
END ;;

DROP PROCEDURE IF EXISTS vehicle_reg;
DELIMITER ;;
CREATE PROCEDURE vehicle_reg (IN counter INT)
BEGIN
	DECLARE i,vId BIGINT;
    set i=1;
    set vId = 1692728651429515265;
    WHILE i<=counter DO
		INSERT INTO vehicle(id,number,color,type,brand,deleted,version) VALUES (vId+i,'车牌号','颜色','C1','品牌',0,1);
        set i = i+1;
	END WHILE;
END ;;

DROP PROCEDURE IF EXISTS vehicle_belong;
DELIMITER ;;
CREATE PROCEDURE vehicle_belong (IN counter INT)
BEGIN
	DECLARE i,belongId,driverId,vId BIGINT;
    set i=1;
    set belongId=1692728651538567169;
    set driverId=1688734750117008361;
    set vId=1692728651429515265;
	While i<=counter DO
		Insert into belong(id,driver_id,vehicle_id,deleted,version) Values (belongId+i,driverId+i,vId+i,0,1);
        set i=i+1;
	End while;
end;

CALL customer_user(1000);
CALL driver_user(1000);
CALL vehicle_reg(1000);
call vehicle_belong(1000);

select * from user;
select phone from user;
select count(*) from user;
select * from customer;
select * from driver;

## insert into user(id,name,phone,password,credit,role_id,deleted,version) values (1688734750117008362,'user1001','13372100021','fcea920f7412b5da7be0cf42b8c93759',1500,2,0,1)
## delete from user where id = 1688734750117008362

##INSERT INTO driver(id,comment_rate,income,user_id,deleted,version,license) VALUES (1688734750427385858,0,0,1688734750117008362,0,1,'C1');
##delete from driver where user_id = 1688734750117008362


