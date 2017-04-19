DROP SCHEMA IF EXISTS `backgammon`;
CREATE SCHEMA `backgammon`;
USE `backgammon`;

DROP TABLE IF EXISTS `users`;

/********** from spring docs **********/

create table users(
	`user_id` BIGINT NOT NULL AUTO_INCREMENT primary key,
	`username` varchar(50) not null,
	`password` varchar(50) not null,
	`enabled` boolean not null,
	`last_modified_date` DATETIME NOT NULL default now(),
    `last_modified_by` varchar(50) NOT NULL default "System",
    `created_date` DATETIME NOT NULL default now(),
    `created_by` varchar(50) NOT NULL default "System"
);








