USE couponzilla_db;

create table IF not exists bluff_history
(
    bluff_history_id bigint auto_increment primary key,
    user_id bigint not null,
    angry_score varchar(50) not null,
    joy_score varchar(50) not null,
    sorrow_score varchar(50) not null,
    surprised_score varchar(50) not null,
    date_added varchar(50) not null

);




