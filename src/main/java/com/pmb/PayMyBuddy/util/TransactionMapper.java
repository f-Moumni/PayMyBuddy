package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.constants.TransactionType;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private String name;

    public TransactionDTO PaymentMapper(Payment payment, OperationType operationType) {
        Account debitAccount = payment.getDebitAccount();
        Account creditAccount = payment.getCreditAccount();
            name = (operationType.equals(OperationType.DEBIT)) ?nameMapper(creditAccount):nameMapper(debitAccount);
        return new TransactionDTO(name, payment.getDateTime(), payment.getDescription(), payment.getAmount(),operationType,TransactionType.PAYMENT);
    }
    public TransactionDTO transferMapper(Transfer transfer, OperationType operationType) {
        BankAccount userBA = transfer.getBankAccount();
        name = userBA.getIban();
        return new TransactionDTO(name, transfer.getDateTime(), transfer.getDescription(),transfer.getAmount(),operationType, TransactionType.TRANSFER);
    }
    private String nameMapper( Account account){
        return account.isEnabled() ?
                account.getAccountOwner().getFirstName() + " " + account.getAccountOwner().getLastName()
                : account.getMail();
    }

}
