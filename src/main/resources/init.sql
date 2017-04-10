DROP SCHEMA IF EXISTS `backgammon_2`;
CREATE SCHEMA `backgammon_2`;
USE `backgammon_2`;

DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;

/********** from spring docs **********/

create table users(
	`username` varchar(50) not null primary key,
	`password` varchar(50) not null,
	`enabled` boolean not null,
	`first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
	`last_modified_date` DATETIME NOT NULL default now(),
    `last_modified_by` varchar(50) NOT NULL default "System",
    `created_date` DATETIME NOT NULL default now(),
    `created_by` varchar(50) NOT NULL default "System"
);

create table authorities (
	`username` varchar(50) not null,
	`authority` varchar(50) not null,
	`last_modified_date` DATETIME NOT NULL default now(),
    `last_modified_by` varchar(50) NOT NULL default "System",
    `created_date` DATETIME NOT NULL default now(),
    `created_by` varchar(50) NOT NULL default "System",
	constraint fk_authorities_users foreign key(username) references users(username)
);









