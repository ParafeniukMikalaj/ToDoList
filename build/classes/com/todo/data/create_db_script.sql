CREATE TABLE `todo`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  PRIMARY KEY  (`id`)
);

CREATE TABLE `todo`.`Task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255),
  `priority_id` INT,
  `creation_date` BIGINT,
  `expiration_date` BIGINT,
  `user_id` INT,
  `delayed_times` INT,
  `folder_id` INT,
  `x` INT,
  `y` INT,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `todo`.`Folder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255),
  `user_id` INT,
  `parent_id` INT,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `todo`.`Priority` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255),
  `user_id` INT,
  PRIMARY KEY  (`id`)
);


ALTER TABLE `todo`.`Task` ADD CONSTRAINT `Task_fk1` FOREIGN KEY (`priority_id`) REFERENCES Priority(`id`);
ALTER TABLE `todo`.`Task` ADD CONSTRAINT `Task_fk2` FOREIGN KEY (`user_id`) REFERENCES User(`id`);
ALTER TABLE `todo`.`Task` ADD CONSTRAINT `Task_fk3` FOREIGN KEY (`folder_id`) REFERENCES Folder(`id`);

