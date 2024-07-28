CREATE TABLE IF NOT EXISTS `driver` (
    `id`       BIGINT NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(255),
    `email_id` VARCHAR(255),
    `password` VARCHAR(255),
    `phone_no` VARCHAR(255),
    `co_ordinate` JSON,
    `registration_type` TINYINT check ( registration_type BETWEEN 0 AND 2 ),
    `vehicle_type` enum ( 'FOUR_SEATER', 'FOUR_SEATER_WITH_KIDS', 'SIX_SEATER' ) NOT NULL,
    `vehicle_number` VARCHAR(255) NOT NULL,
    `driver_license_info` json,
    `availability_status` enum ( 'AVAILABLE', 'BREAK', 'OFF_WORK', 'ON_CALL' ) NOT NULL,
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `date_modified` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `ride_order` (
    `id`         BIGINT NOT NULL AUTO_INCREMENT,
    `pickup_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `destination` JSON,
    `source`      JSON,
    `ride_status` enum ( 'AVAILABLE', 'BOOKED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `date_modified` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `driver_ride_relation` (
    `id`         BIGINT NOT NULL AUTO_INCREMENT,
    `driver_id` BIGINT NOT NULL,
    `ride_id` BIGINT NOT NULL,
    `assigned_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `date_modified` DATETIME DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (`driver_id`) REFERENCES driver(`id`),
     FOREIGN KEY (`ride_id`) REFERENCES ride_order(`id`),
    CONSTRAINT `unique_driver_ride` UNIQUE (`driver_id`, `ride_id`)
);

CREATE TABLE IF NOT EXISTS `ride_state_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ride_id` BIGINT NOT NULL,
    `ride_status` VARCHAR(50) NOT NULL,
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `changed_by` VARCHAR(100) NULL,
    FOREIGN KEY (`ride_id`) REFERENCES ride_order(`id`)
);