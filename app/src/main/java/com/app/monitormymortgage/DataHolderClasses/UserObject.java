package com.app.monitormymortgage.DataHolderClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Sangram on 19/04/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject {
    static String firstName;
    static String lastName;
    static String email;
    static String phoneNumber;
    static String countryCode;
    static String notifyRateIncrease;
    static String contactPreferences;
    static String notifyUntilMaturity;
    static String notifySavingPerMonth;
    static String notifySavingOverTerm;
    static String opportunityNotificationPrefernce;

    public static String getContactPreferences() {
        return contactPreferences;
    }

    public void setContactPreferences(String contactPreferences) {
        this.contactPreferences = contactPreferences;
    }

    public static String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static String getNotifyRateIncrease() {
        return notifyRateIncrease;
    }

    public void setNotifyRateIncrease(String notifyRateIncrease) {
        this.notifyRateIncrease = notifyRateIncrease;
    }

    public static String getNotifySavingOverTerm() {
        return notifySavingOverTerm;
    }

    public void setNotifySavingOverTerm(String notifySavingOverTerm) {
        this.notifySavingOverTerm = notifySavingOverTerm;
    }

    public static String getNotifySavingPerMonth() {
        return notifySavingPerMonth;
    }

    public void setNotifySavingPerMonth(String notifySavingPerMonth) {
        this.notifySavingPerMonth = notifySavingPerMonth;
    }

    public static String getNotifyUntilMaturity() {
        return notifyUntilMaturity;
    }

    public void setNotifyUntilMaturity(String notifyUntilMaturity) {
        this.notifyUntilMaturity = notifyUntilMaturity;
    }

    public static String getOpportunityNotificationPrefernce() {
        return opportunityNotificationPrefernce;
    }

    public void setOpportunityNotificationPrefernce(String opportunityNotificationPrefernce) {
        this.opportunityNotificationPrefernce = opportunityNotificationPrefernce;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
