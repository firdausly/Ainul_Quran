package com.AinulQuran.model;




public class billTransactions {

    public String billName;
    public String billDescription;
    public String billTo;
    public String billEmail;
    public String billPhone;
    public String billStatus;
    public String billpaymentStatus;
    public String billpaymentAmount;
    public String billpaymentInvoiceNo;
    public String billPaymentDate;

    @Override
    public String toString() {
        return "billTransactions{" +
                "billName='" + billName + '\'' +
                ", billDescription='" + billDescription + '\'' +
                ", billTo='" + billTo + '\'' +
                ", billEmail='" + billEmail + '\'' +
                ", billPhone='" + billPhone + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", billpaymentStatus='" + billpaymentStatus + '\'' +
                ", billpaymentAmount='" + billpaymentAmount + '\'' +
                ", billpaymentInvoiceNo='" + billpaymentInvoiceNo + '\'' +
                ", billPaymentDate='" + billPaymentDate + '\'' +
                '}';
    }
}
