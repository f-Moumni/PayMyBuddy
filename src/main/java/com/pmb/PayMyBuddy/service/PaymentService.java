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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

/**
 * contain all business service methods for payment
 */
@Service
@Transactional
public class PaymentService implements IPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
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
     * {@inheritDoc}
     */
    @Override
    public boolean doPayment(PaymentDTO paymentDTO) throws InsufficientFundsException {

        Account accountDebit = accountRepository.findByMail(principalUser.getCurrentUserMail()).get();
        Account accountCredit = accountRepository.findByMail(paymentDTO.getCreditAccountEmail()).get();
        logger.info("saving payment to {} {}", accountCredit.getAccountOwner().getFirstName(),
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
                logger.error("pour balance ");
                throw new InsufficientFundsException("poor balance");
            }
            updatePMBAccount(fee);// update admin account
            return true;
        }
        return false;

    }

    /**
     * get all sent payments
     *
     * @return
     */
    private List<TransactionDTO> getSentPayments() {
logger.debug("get all sent payments");
        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByDebitAccount(principalUser.getCurrentUserMail()).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.DEBIT))
                .collect(Collectors.toList());
    }

    /**
     * get all received payments
     *
     * @return
     */
    private List<TransactionDTO> getReceivedPayments() {
        logger.debug("get all received payments");
        List<Payment> payments = new ArrayList<>();
        paymentRepository.findByCreditAccount(principalUser.getCurrentUserMail()).forEach(payments::add);
        return payments.stream()
                .map(payment -> transactionMapper.PaymentMapper(payment, OperationType.CREDIT))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionDTO> getAllPayments() {
        logger.debug("get all payments");
        return Stream.concat(getReceivedPayments().stream()
                , getSentPayments().stream()
        ).sorted(Comparator.comparing(TransactionDTO::getDateTime).reversed()).collect(Collectors.toList());
    }

    /**
     * update admin account
     *
     * @param fee
     */
    private void updatePMBAccount(double fee) {
        logger.debug("update admin account");
        Account pmbAccount = accountRepository.findById(1L).get();
        pmbAccount.setBalance(pmbAccount.getBalance() + fee);
        accountRepository.save(pmbAccount);
    }
}
