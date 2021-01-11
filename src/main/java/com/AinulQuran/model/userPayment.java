package com.AinulQuran.model;


import javax.persistence.*;

@Entity
@Table(name="user_payment")
public class userPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String status;

    @Column(name = "billcode")
    private String billcode;

    public userPayment() {
    }

    public userPayment(Long id, String username, String status, String billCode) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.billcode = billCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBillCode() {
        return billcode;
    }

    public void setBillCode(String billCode) {
        this.billcode = billCode;
    }
}
