package com.pmb.PayMyBuddy.service;

import com.pmb.PayMyBuddy.constants.OperationType;
import com.pmb.PayMyBuddy.DTO.PaymentDTO;
import com.pmb.PayMyBuddy.DTO.TransactionDTO;
import com.pmb.PayMyBuddy.exceptions.InsufficientFundsException;
import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.Payment;
import com.pmb.PayMyBuddy.repository.AccountRepository;
import com.pmb.PayMyBuddy.repository.PaymentRepository;
import com.pmb.PayMyBuddy.security.PrincipalUser;
import com.pmb.PayMyBuddy.util.Calculator;
import com.pmb.PayMyBuddy.util.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@Service
@Transactional
@Slf4j
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final Calculator calculator;
    private final PrincipalUser principalUser;
    private double fee;
    private double total;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository, TransactionMapper transactionMapper, Calculator calculator, PrincipalUser principalUser) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.calculator = calculator;
        this.principalUser = principalUser;
    }

    /**
     * send money to contact
     *
     * @param paymentDTO
     * @return tue is payment is done
     * @throws InsufficientFundsException
     */
@Override
    public boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException {

        Account accountDebit = accountRepository.findByMail(principalUser.getCurrentUserMail()).get();
        Account accountCredit = accountRepository.findByMail(paymentDTO.getCreditAccountEmail()).get();
        log.info("saving payment to {} {}", accountCredit.getAccountOwner().getFirstName(),
                accountCredit.getAccountOwner().getLastName());
        if (accountDebit.isEnabled() &&//account is active
                accountCredit.isEnabled() && accountDebit.getAccountOwner().getContacts()
                .contains(accountCredit.getAccountOwner())) {// credit account is in contact list
            fee = calculator.feeCalculator(paymentDTO.getAmount());
            total = calculator.totalCalculator(paymentDTO.getAmount());
            if (accountDebit.getBalance() >= total) { // account has enough balance
                accountCredit.setBalance(accountCredit.getBalance() + paymentDTO.getAmount());
                accountDebit.setBalance(accountDebit.getBalance() - total);
                accountRepository.save(accountCredit);
                accountRepository.save(accountDebit);
                paymentRepository.save(
                        new Payment(paymentDTO.getAmount(), fee, paymentDTO.getDescription(), now(), accountDebit, accountCredit));
            } else {
                throw new InsufficientFundsException("poor balance");
            }
            updatePMBAccount(fee);// update admin account
            return true;
        }
return false;

    }

    private List<TransactionDTO> getSentPayments() {

        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByDebitAccount(principalUser.getCurrentUserMail()).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.DEBIT))
                .collect(Collectors.toList());
    }

    private List<TransactionDTO> getReceivedPayments() {
        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByCreditAccount(principalUser.getCurrentUserMail()).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.CREDIT))
                .collect(Collectors.toList());


    }
    @Override
    public List<TransactionDTO> getAllPayments() {
        return Stream.concat(getReceivedPayments().stream()
                , getSentPayments().stream()
        ).collect(Collectors.toList());
    }

    private void updatePMBAccount(double fee) {
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
