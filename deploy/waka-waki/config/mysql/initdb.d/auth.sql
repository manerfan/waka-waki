CREATE DATABASE IF NOT EXISTS `waki_auth`;

GRANT ALL privileges ON `waki_auth`.* TO 'waka-waki'@'%' WITH GRANT OPTION;

flush privileges;

USE `waki_auth`;
