--   Database: db_payMyBuddy
-- ------------------------------------------------------

DROP DATABASE IF EXISTS db_payMyBuddy;
CREATE DATABASE `db_payMyBuddy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE db_payMyBuddy;
-- ------------------------------------------------------
--
-- Table structure for table `role`
--
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `role_id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(10) COLLATE utf8_bin DEFAULT NULL,
                        PRIMARY KEY (`role_id`),
                        UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
--
-- Dumping data for table `role`
--
INSERT INTO `role` (`name`) VALUES ('USER');
INSERT INTO `role` (`name`) VALUES ('ADMIN');

--
-- Table structure for table `user`
--
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `iduser` bigint NOT NULL AUTO_INCREMENT,
                        `first_name` varchar(255) COLLATE utf8_bin NOT NULL,
                        `last_name` varchar(255) COLLATE utf8_bin NOT NULL,
                        `birthdate` date DEFAULT NULL,
                        PRIMARY KEY (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
--
-- Dumping data for table `user`
--
INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('pmb', 'account', '1954-10-01');
INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('thomas', 'doe', '1954-10-01');
INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('john', 'doe', '1954-10-01');

--
-- Table structure for table `pmb_account`
--
DROP TABLE IF EXISTS `pmb_account`;
CREATE TABLE `pmb_account` (
                               `account_id` bigint NOT NULL AUTO_INCREMENT,
                               `balance` double NOT NULL,
                               `enabled` tinyint(1) NOT NULL DEFAULT '1',
                               `email` varchar(255) COLLATE utf8_bin NOT NULL,
                               `password` varchar(60) COLLATE utf8_bin DEFAULT NULL,
                               `owner` bigint,
                               `role` bigint,
                               PRIMARY KEY (`account_id`),
                               UNIQUE KEY `UK_2h2q0wctl6qxe7iawc42fml1d` (`email`),
                               CONSTRAINT `fk_role`  FOREIGN KEY (`role`) REFERENCES `db_payMyBuddy`.`role` (`role_id`),
                               CONSTRAINT `fk_account_owner` FOREIGN KEY (`owner`) REFERENCES `user` (`iduser`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

--
-- Dumping data for table `pmb_account`
--
INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('200', '1', 'pmb@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '1', '2');
INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('50', '1', 'doe@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '2', '1');
INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('100', '1', 'john@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '3', '1');

--
-- Table structure for table `contact`
--
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
                           `owner_id` bigint NOT NULL,
                           `contact_id` bigint NOT NULL,
                           PRIMARY KEY (`owner_id`,`contact_id`),
                           CONSTRAINT `fk_contact` FOREIGN KEY (`contact_id`) REFERENCES `user` (`iduser`) ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT `fk_contact_owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`iduser`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
--
-- Dumping data for table `contact`
--
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('3', '2');
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('2', '3');

--
-- Table structure for table `bank_account`
--
DROP TABLE IF EXISTS `bank_account`;
CREATE TABLE `bank_account` (
                                `idbank_account` bigint NOT NULL AUTO_INCREMENT,
                                `iban` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                                `swift` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                                `owner` bigint DEFAULT NULL,
                                PRIMARY KEY (`idbank_account`),
                                UNIQUE KEY `iban_UNIQUE` (`iban`),
                                UNIQUE KEY `swift_UNIQUE` (`swift`) /*!80000 INVISIBLE */,
                                UNIQUE KEY `owner_UNIQUE` (`owner`),
                                CONSTRAINT `fk_bank_owner` FOREIGN KEY (`owner`) REFERENCES `user` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
--
-- Dumping data for table `bank_account`
--
INSERT INTO `bank_account` (`iban`, `swift`, `owner`) VALUES ('iban111111', 'swift111111', '1');
INSERT INTO `bank_account` (`iban`, `swift`, `owner`) VALUES ('iban222222', 'swift22222', '2');

--
-- Table structure for table `transaction`
--
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
                               `idtransaction` bigint NOT NULL AUTO_INCREMENT,
                               `transaction_type` varchar(31) COLLATE utf8_bin NOT NULL,
                               `amount` double NOT NULL,
                               `date` datetime NOT NULL,
                               `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                               `fee` double NOT NULL,
                               `credit_account` bigint DEFAULT NULL,
                               `debit_account` bigint DEFAULT NULL,
                               `bankaccount` bigint DEFAULT NULL,
                               PRIMARY KEY (`idtransaction`),
                               CONSTRAINT `fk_bankaccount` FOREIGN KEY (`bankaccount`) REFERENCES `bank_account` (`idbank_account`) ON UPDATE CASCADE,
                               CONSTRAINT `fk_creditaccount` FOREIGN KEY (`credit_account`) REFERENCES `pmb_account` (`account_id`) ON UPDATE CASCADE,
                               CONSTRAINT `fk_debitaccount` FOREIGN KEY (`debit_account`) REFERENCES `pmb_account` (`account_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

--
-- Dumping data for table `transaction`
--
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES ('PAYMENT', '88', '2022-04-24 11:45:09', 'shopping', '0.44', '2', '3');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES ('PAYMENT', '25', '2022-04-24 11:45:55', 'shopping', '0.125', '3', '2');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `bankaccount`) VALUES ('TRANSFER', '40', '2022-04-17 22:48:12', 'mobile', '0.2', '2', '2');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `debit_account`, `bankaccount`) VALUES ('TRANSFER', '40', '2022-04-17 22:48:12', 'movie', '0.2', '2', '2');