package com.app.monitormymortgage.DataHolderClasses;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Sangram on 06/05/16.
 */
public class MortgageDetailsHolder {

    private String amortizedBalance;

    private String interestRate;

    private String payment;

    private String paymentFrequency;

    private String maturityDate;

    private String amortization;

    private String term;

    private String status;

    private String type;

    private String postalCode;

    private String creditLineAmt;

    private String mortgageStatus;

    private String updatedAt;

    private String attachmentCount;

    String opprCreatedDate;


    public String getAttachmentCount() {
        return attachmentCount;
    }

    @JsonProperty("attachments_count")
    public void setAttachmentCount(String attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public String getOpprCreatedDate() {
        return opprCreatedDate;
    }

    @JsonProperty("opportunity_created_date")
    public void setOpprCreatedDate(String opprCreatedDate) {
        this.opprCreatedDate = opprCreatedDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMortgageStatus() {
        return mortgageStatus;
    }

    @JsonProperty("mortgage_opportunity_status")
    public void setMortgageStatus(String mortgageStatus) {
        this.mortgageStatus = mortgageStatus;
    }

    public String getAmortization() {
        return amortization;
    }

    @JsonProperty("amortization_period")
    public void setAmortization(String amortization) {
        this.amortization = amortization;
    }

    public String getAmortizedBalance() {
        return amortizedBalance;
    }

    @JsonProperty("amortized_amount")
    public void setAmortizedBalance(String amortizedBalance) {
        this.amortizedBalance = amortizedBalance;
    }

    public String getCreditLineAmt() {
        return creditLineAmt;
    }

    @JsonProperty("credit_line_amount")
    public void setCreditLineAmt(String creditLineAmt) {
        this.creditLineAmt = creditLineAmt;
    }

    public String getInterestRate() {
        return interestRate;
    }

    @JsonProperty("interest_rate_percentage")
    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    @JsonProperty("maturity_date_utc")
    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getPayment() {
        return payment;
    }

    @JsonProperty("current_payment_amount")
    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    @JsonProperty("current_payment_frequency")
    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("property_postal_code")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("current_term_rate_type")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerm() {
        return term;
    }

    @JsonProperty("current_term_in_months")
    public void setTerm(String term) {
        this.term = term;
    }

    public String getType() {
        return type;
    }

    @JsonProperty("current_term_type")
    public void setType(String type) {
        this.type = type;
    }
}
