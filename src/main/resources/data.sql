drop table if exists user CASCADE;
drop table if exists diary CASCADE ;
drop table if exists character CASCADE ;
drop table if exists sleep CASCADE ;

create table user (
    user_pk bigint generated by default as identity,
    user_role_type varchar(255),
    user_age bigint,
    user_id varchar(255),
    user_message varchar(255),
    user_nick_name varchar(20),
    user_password varchar(255),
    user_money bigint,
    user_goal_sleep_time time,
    user_goal_wake_time time,
    character_fk bigint,
    primary key (user_pk)
);

create table character (
   character_pk bigint generated by default as identity,
   character_color varchar(255),
   character_experience integer,
   character_level bigint,
   character_speech_bubble varchar(255),
   character_status varchar(255),
   primary key (character_pk)
);

create table diary (
   diary_pk bigint generated by default as identity,
   diary_content text,
   diary_delete_date date,
   diary_writing_time timestamp,
   diary_date date,
   user_fk bigint,
   primary key (diary_pk)
);

create table sleep (
   sleep_pk bigint generated by default as identity,
   actual_sleep_time TIMESTAMP,
   actual_wake_time TIMESTAMP,
   set_sleep_time TIMESTAMP,
   set_wake_time TIMESTAMP,
   user_fk bigint,
   primary key (sleep_pk)
);

-- INSERT INTO SLEEP (actual_sleep_time, actual_wake_time, set_sleep_time, set_wake_time) VALUES ()
INSERT INTO USER (USER_ROLE_TYPE, USER_AGE, USER_ID, USER_MESSAGE, USER_NICK_NAME, USER_PASSWORD, USER_MONEY, user_goal_wake_time, user_goal_sleep_time, character_fk) VALUES ('ADMIN',24,'sleeper','관리자다','관리자','sleeper123@',0,'07:30','23:30',1);
INSERT INTO character (character_color, character_experience, character_level, character_speech_bubble, character_status) VALUES ('GRAY',0,1,'안녕하세요 관리자님','NO_SLEEP');
INSERT INTO DIARY (diary_content, diary_date, diary_writing_time, user_fk) VALUES ('행복한 하루 되세요.','2022-12-25','2022-12-26T12:36',1);