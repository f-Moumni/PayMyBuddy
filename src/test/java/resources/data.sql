DELETE FROM  `user`;
DELETE FROM  `role`;
DELETE FROM  `pmb_account`;
DELETE FROM  `contact`;
DELETE FROM  `bank_account`;
DELETE FROM  `transaction`;


INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('pmb', 'account', '1954-10-01');
INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('thomas', 'doe', '1954-10-01');
INSERT INTO `user` (`first_name`, `last_name`, `birthdate`) VALUES ('joe', 'doe', '1954-10-01');

INSERT INTO `role` (`name`) VALUES ('USER');
INSERT INTO `role` (`name`) VALUES ('ADMIN');

INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('200', '1', 'pmb@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '1', '1');
INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('100', '1', 'thomas@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '2', '1');
INSERT INTO `pmb_account` (`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES ('100', '1', 'joe@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '3', '1');


INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('1', '2');
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('2', '1');
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('3', '2');
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('2', '3');

INSERT INTO `bank_account` (`iban`, `swift`, `owner`) VALUES ('iban111111', 'swift111111', '1');
INSERT INTO `bank_account` (`iban`, `swift`, `owner`) VALUES ('iban222222', 'swift22222', '2');
INSERT INTO `bank_account` (`iban`, `swift`, `owner`) VALUES ('iban333333', 'swift333333', '3');


INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES ('PAYMENT', '88', '2022-04-24 11:45:09', 'shopping', '0.44', '2', '3');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES ('PAYMENT', '25', '2022-04-24 11:45:55', 'shopping', '0.125', '3', '2');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `bankaccount`) VALUES ('TRANSFER', '40', '2022-04-17 22:48:12', 'mobile', '0.2', '2', '2');
INSERT INTO `transaction` (`transaction_type`, `amount`, `date`, `description`, `fee`, `debit_account`, `bankaccount`) VALUES ('TRANSFER', '40', '2022-04-17 22:48:12', 'mobile', '0.2', '3', '3');