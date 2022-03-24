package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.BankAccount;
import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private String name;
    private String amount;

    public TransactionDTO PaymentMapper(Payment payment, OperationType operationType) {
        Account debitAccount = payment.getDebitAccount();
        Account creditAccount = payment.getCreditAccount();
        if (operationType.equals(OperationType.DEBIT)) {
            amount = "-" + payment.getAmount();
            name = creditAccount.isActive() ?
                    creditAccount.getAccountOwner().getFirstName() + " " + creditAccount.getAccountOwner().getLastName()
                    : creditAccount.getMail();

        } else {
            name = debitAccount.isActive() ?
                    debitAccount.getAccountOwner().getFirstName() + " " + debitAccount.getAccountOwner().getLastName()
                    : debitAccount.getMail();
            amount = "+" + payment.getAmount();
        }
        return new TransactionDTO(name, payment.getDateTime(), payment.getDescription(), amount);
    }


    public TransactionDTO transferMapper(Transfer transfer, OperationType operationType) {
        BankAccount userBA = transfer.getBankAccount();
        name = userBA.getIban();
        return new TransactionDTO(name, transfer.getDateTime(), transfer.getDescription(),
                (operationType.equals(OperationType.DEBIT)) ? "-" + transfer.getAmount():"+" + transfer.getAmount());
    }

}
