package com.pmb.PayMyBuddy.model;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "user")
public class User {
    /**
     * use id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private long userID;

    /**
     * user's first name
     **/
    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "the first name cannot be empty or null")
    private String firstName;

    /**
     * user's last name
     **/
    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "the last name cannot be empty or null")
    private String lastName;

    /**
     * user's birthdate
     **/
    @Column(name = "birthdate")
    private LocalDate birthDate;

    /**
     * user's pay my buddy account
     **/
    @OneToOne(mappedBy = "accountOwner", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private Account account;

    /**
     * user's bank accounts
     **/
    @OneToOne(
            mappedBy = "owner", cascade = CascadeType.ALL
    )
    private BankAccount bankAccount;

    /**
     * pay may buddy account's contacts
     **/
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
            }
    )
    @JoinTable(
            name = "contact",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )

    private Set<User> contacts = new TreeSet<>();

    public User(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public void removeAccount(User user) {
        user.account.setActive(false);
        user.account.setPassword(null);
    }

    public void addContact(User user) {
        user.getContacts().add(this);
        contacts.add(user);
    }

    public void removeContact(User user) {
        user.getContacts().remove(this);
        contacts.remove(user);
    }
}
