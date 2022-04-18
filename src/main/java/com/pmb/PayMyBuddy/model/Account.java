package com.pmb.PayMyBuddy.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity()
@Table(name = "pmb_account")
public class Account   {
    /**
     * pay may buddy account's id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long id;

    /**
     * account's email
     **/
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "the email cannot be empty or null")
    @Email(message = "please entre email address")
    private String mail;

    /**
     * account's password
     */
    @NotBlank(message = "the password cannot be empty or null")
    private String password;

    /**
     * pay may buddy account's balance
     **/
    @Column( nullable = false)
    private double balance;

    /**
     * pay may buddy account's status
     **/
    private boolean enabled ;

    /**
     * pay may buddy locked status
     **/
    @OneToOne
    @JoinColumn(name = "owner")
    private AppUser accountOwner;

    @ManyToOne
    @JoinColumn(name ="role")
     private Role role ;
    /**
     * All payments received in pay may buddy account
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "creditAccount",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
    private Set<Payment> receivedPayments = new TreeSet<>();


    /**
     * All payments sent from pay may buddy account to contacts
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "debitAccount",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }
    )
    private Set<Payment> paymentsSent = new TreeSet<>();

    /**
     * All transfer  from /to bankAccount
     **/
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "bankAccount",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }
    )
    private Set<Transfer> BankTransfer = new TreeSet<>();
    /**
     * Constructor
     * @param mail
     * @param password
     * @param accountOwner
     */
    public Account(String mail, String password, AppUser accountOwner) {
        this.mail = mail;
        this.password = password;
        this.accountOwner = accountOwner;
    }




    /**
     * add new Received Payment
     *
     * @param payment

    public void addReceivedPayments(Payment payment) {
        this.receivedPayments.add(payment);
        payment.setCreditAccount(this);
    }
*/
    /**
     * add new sent Payment
     *
     * @param payment

    public void addPaymentsSent(Payment payment) {
        paymentsSent.add(payment);
        payment.setDebitAccount(this);
    }
  */
}
