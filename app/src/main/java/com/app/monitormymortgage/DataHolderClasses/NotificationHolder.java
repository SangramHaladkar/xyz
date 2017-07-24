package com.app.monitormymortgage.DataHolderClasses;

/**
 * Created by Sangram on 22/05/16.
 */
public class NotificationHolder {
    String mortgageTitle;
    String opportunityTitle;
    String opporCreatedDate;
    String notificationId;
    String isRead;
    String opportunityId;
    String type;
    String notification_type_code;

    public String getNotification_msg() {
        return notification_msg;
    }

    public void setNotification_msg(String notification_msg) {
        this.notification_msg = notification_msg;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_type_code() {
        return notification_type_code;
    }

    public void setNotification_type_code(String notification_type_code) {
        this.notification_type_code = notification_type_code;
    }

    String notification_title;
    String notification_msg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    String isExpired;


    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }


    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }


    public String getMortgageTitle() {
        return mortgageTitle;
    }

    public void setMortgageTitle(String mortgageTitle) {
        this.mortgageTitle = mortgageTitle;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getOpporCreatedDate() {
        return opporCreatedDate;
    }

    public void setOpporCreatedDate(String opporCreatedDate) {
        this.opporCreatedDate = opporCreatedDate;
    }

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }


}
