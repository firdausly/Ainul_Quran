package com.AinulQuran.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class createBill {


    private String userSecretKey;
    private String categoryCode;
    private String billName;
    private String billDescription;
    private final int billPriceSetting=1;
    private final int billPayorInfo=1;
    private int billAmount;
    private String billReturnUrl;
    private String billCallbackUrl;
    private String billExternalReferenceNo;

    @NotEmpty
    private String billTo;

    @NotEmpty
    @Email
    private String billEmail;

    @NotEmpty
    private String billPhone;

    private final int billSplitPayment=0;
    private String billSplitPaymentArgs;
    private final int billPaymentChannel=0;
    private final int billDisplayMerchant=1;
    private String billContentEmail;
    private final int billChargetoCustomer=1;


    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
    }

    public int getBillPriceSetting() {
        return billPriceSetting;
    }

    public int getBillPayorInfo() {
        return billPayorInfo;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(int billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillReturnUrl() {
        return billReturnUrl;
    }

    public void setBillReturnUrl(String billReturnUrl) {
        this.billReturnUrl = billReturnUrl;
    }

    public String getBillCallbackUrl() {
        return billCallbackUrl;
    }

    public void setBillCallbackUrl(String billCallbackUrl) {
        this.billCallbackUrl = billCallbackUrl;
    }

    public String getBillExternalReferenceNo() {
        return billExternalReferenceNo;
    }

    public void setBillExternalReferenceNo(String billExternalReferenceNo) {
        this.billExternalReferenceNo = billExternalReferenceNo;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getBillEmail() {
        return billEmail;
    }

    public void setBillEmail(String billEmail) {
        this.billEmail = billEmail;
    }

    public String getBillPhone() {
        return billPhone;
    }

    public void setBillPhone(String billPhone) {
        this.billPhone = billPhone;
    }

    public int getBillSplitPayment() {
        return billSplitPayment;
    }

    public String getBillSplitPaymentArgs() {
        return billSplitPaymentArgs;
    }

    public void setBillSplitPaymentArgs(String billSplitPaymentArgs) {
        this.billSplitPaymentArgs = billSplitPaymentArgs;
    }

    public int getBillPaymentChannel() {
        return billPaymentChannel;
    }

    public int getBillDisplayMerchant() {
        return billDisplayMerchant;
    }

    public String getBillContentEmail() {
        return billContentEmail;
    }

    public void setBillContentEmail(String billContentEmail) {
        this.billContentEmail = billContentEmail;
    }

    public int getBillChargetoCustomer() {
        return billChargetoCustomer;
    }
}
