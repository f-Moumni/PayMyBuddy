package com.pmb.PayMyBuddy.util;


import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {


    public TransactionDTO paymentSentMapper(Payment payment ){

        return new TransactionDTO(payment.getDebitAccount().getAccountOwner().getFirstName() +" "
                + payment.getDebitAccount().getAccountOwner().getLastName(),
                payment.getDateTime(), payment.getDescription(),payment.getFee(),"-"+payment.getAmount());

    }
    public TransactionDTO paymentReceivedMapper(Payment payment ) {
        return new TransactionDTO((payment.getDebitAccount().isActive()) ?
                payment.getCreditAccount().getAccountOwner().getFirstName() + " "
                        + payment.getCreditAccount().getAccountOwner().getLastName() : payment.getCreditAccount().getMail(),
                payment.getDateTime(), payment.getDescription(), payment.getFee(), "+" + payment.getAmount());
    }
    public TransactionDTO bankTransfers(Transfer transfer ) {
        return new TransactionDTO(transfer.getCreditBankAccount().getIban(),
                transfer.getDateTime(), transfer.getDescription(),transfer.getFee(),"+"+ transfer.getAmount());
    }


}
