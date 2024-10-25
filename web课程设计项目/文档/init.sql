insert into user_type ( name,id) values( 'ROLE_ADMIN',1);
insert into user_type ( name,id) values( 'ROLE_STUDENT',2);
insert into user_type ( name,id) values( 'ROLE_TEACHER',3);
insert into person (per_num, per_name,person_id,user_type_id) values( 'admin','admin',1,1);
insert into user (user_name, password,person_id,user_type_id,user_id) values( 'admin','$2a$10$FV5lm..jdQWmV7hFguxKDeTrGyiWg1u6HYD2QiQc0tRROrNtSQVOy',1,1,1);
insert into admin (grade,person_id,user_id) values (2,1,1)