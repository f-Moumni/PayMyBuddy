package com.pmb.PayMyBuddy.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name ;
    @OneToMany( mappedBy =  "role",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
private List<Account> accounts = new ArrayList<>();

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {

    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}