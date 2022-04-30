INSERT INTO `user` (`iduser`,`first_name`, `last_name`, `birthdate`) VALUES (1,'pmb', 'account', '1954-10-01');
INSERT INTO `user` (`iduser`,`first_name`, `last_name`, `birthdate`) VALUES (2,'thomas', 'doe', '1954-10-01');
INSERT INTO `user` (`iduser`,`first_name`, `last_name`, `birthdate`) VALUES (3,'john', 'doe', '1954-10-01');

INSERT INTO `role` (`role_id`,`name`) VALUES (1,'USER');
INSERT INTO `role` (`role_id`,`name`) VALUES (2,'ADMIN');

INSERT INTO `pmb_account` (`account_id`,`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES (1,'200', '1', 'pmb@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '1', '2');
INSERT INTO `pmb_account` (`account_id`,`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES (2,'0', '1', 'doe@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '2', '1');
INSERT INTO `pmb_account` (`account_id`,`balance`, `enabled`, `email`, `password`, `owner`, `role`) VALUES (3,'100', '1', 'john@exemple.fr', '$2a$10$CTPq2xvwt2f.lSmKqWdUYe8nyqoQNvX2RXKtoH09p.s/z7qbkeoby', '3', '1');

INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('3', '2');
INSERT INTO `contact` (`owner_id`, `contact_id`) VALUES ('2', '3');

INSERT INTO `bank_account` (`idbank_account`,`iban`, `swift`, `owner`) VALUES (1,'iban111111', 'swift111111', '1');
INSERT INTO `bank_account` (`idbank_account`,`iban`, `swift`, `owner`) VALUES (2,'iban222222', 'swift22222', '2');
INSERT INTO `bank_account` (`idbank_account`,`iban`, `swift`, `owner`) VALUES (3,'iban333333', 'swift333333', '3');


INSERT INTO `transaction` (`idtransaction`,`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES (1,'PAYMENT', '88', '2022-04-24 11:45:09', 'shopping', '0.44', '2', '3');
INSERT INTO `transaction` (`idtransaction`,`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `debit_account`) VALUES (2,'PAYMENT', '25', '2022-04-24 11:45:55', 'shopping', '0.125', '3', '2');
INSERT INTO `transaction` (`idtransaction`,`transaction_type`, `amount`, `date`, `description`, `fee`, `credit_account`, `bankaccount`) VALUES (3,'TRANSFER', '40', '2022-04-17 22:48:12', 'mobile', '0.2', '2', '2');
INSERT INTO `transaction` (`idtransaction`,`transaction_type`, `amount`, `date`, `description`, `fee`, `debit_account`, `bankaccount`) VALUES (4,'TRANSFER', '40', '2022-04-17 22:48:12', 'mobile', '0.2', '3', '3');