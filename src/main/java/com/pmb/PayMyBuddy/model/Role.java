package com.pmb.PayMyBuddy.model;

import com.pmb.PayMyBuddy.constants.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
   // @Enumerated(EnumType.STRING)
    private String name ;
    @OneToMany( mappedBy =  "role",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
private List<Account> accounts = new ArrayList<>();



}