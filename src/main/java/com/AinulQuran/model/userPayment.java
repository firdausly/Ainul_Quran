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

    @Column(name = "invoiceno")
    private String invoiceno;

    @Column(name = "amountreceived")
    private String amountreceived;

    public String getAmountreceived() {
        return amountreceived;
    }

    public void setAmountreceived(String amountreceived) {
        this.amountreceived = amountreceived;
    }

    public userPayment() {
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

    public String getBillcode() {
        return billcode;
    }

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    @Override
    public String toString() {
        return "userPayment{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                ", billcode='" + billcode + '\'' +
                ", invoiceno='" + invoiceno + '\'' +
                ", amountreceived='" + amountreceived + '\'' +
                '}';
    }
}
