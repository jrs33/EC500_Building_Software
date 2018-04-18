DROP DATABASE IF EXISTS `videoMaker`;
CREATE DATABASE `videoMaker`;

USE `videoMaker`;

CREATE TABLE twitter (date DATETIME, num_images INTEGER);
CREATE TABLE google (date DATETIME, label VARCHAR(50));
CREATE TABLE ffmpeg (date DATETIME, url VARCHAR(150));