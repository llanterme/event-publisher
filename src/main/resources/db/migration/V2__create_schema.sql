-- noinspection SqlNoDataSourceInspectionForFile

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS knox_db DEFAULT CHARACTER SET utf8;

USE couponzilla_db;

create table IF not exists organization
(
    organization_id bigint auto_increment primary key,
    name varchar(50) null,
    industry_id varchar(50) not null,
    contact varchar(50) not null,
    email varchar(50) not null,
    website varchar(50) not null,
    description varchar(50) not null,
    date_registered varchar(50) not null

);

CREATE TABLE IF NOT EXISTS user (

    user_id bigint auto_increment primary key,
    name varchar(50) not null,
    email_address varchar(50) not null,
    date_registered varchar(50) not null

);

CREATE TABLE IF NOT EXISTS coupon (

    coupon_id bigint auto_increment primary key,
    organization_id bigint not null,
    industry_id bigint not null,
    coupon varchar(50) not null,
    description varchar(3000) not null

);

CREATE TABLE IF NOT EXISTS industry (

    industry_id bigint auto_increment primary key,
    industry varchar(3000) not null

);

CREATE TABLE IF NOT EXISTS redemption (

    redemption_id bigint auto_increment primary key,
    coupon_id bigint not null,
    user_id bigint not null,
    date_redeemed varchar(50) not null

);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

