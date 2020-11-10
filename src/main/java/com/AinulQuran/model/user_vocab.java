package com.AinulQuran.model;

import javax.persistence.*;

@Entity
@Table(name="user_vocab")
public class user_vocab {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String vocab;

    public user_vocab( String username, String vocab) {
        this.username = username;
        this.vocab = vocab;
    }
    public user_vocab() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }
}


