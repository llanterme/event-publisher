USE couponzilla_db;

create table IF not exists coupon_history
(
    coupon_history_id bigint auto_increment primary key,
    user_id bigint not null,
    coupon_id bigint not null,
    date_added varchar(50) not null

);


ALTER TABLE organization MODIFY COLUMN industry_id bigint not null;
ALTER TABLE coupon ADD COLUMN active varchar(50) Not null;
DROP TABLE redemption;



