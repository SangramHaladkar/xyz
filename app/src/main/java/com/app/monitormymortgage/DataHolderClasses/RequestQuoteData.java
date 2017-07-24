package com.app.monitormymortgage.DataHolderClasses;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Sangram on 12/01/17.
 */

public class RequestQuoteData {

    private String request_id;

    private String title;

    private String quote_type;

    private String quote_type_code;

    private String mortgage_opportunity_status;

    private String owner;

    private String opportunity_created_at;

    private String opportunity_created_date;

    public String getMortgage_opportunity_status() {
        return mortgage_opportunity_status;
    }

    @JsonProperty("mortgage_opportunity_status")
    public void setMortgage_opportunity_status(String mortgage_opportunity_status) {
        this.mortgage_opportunity_status = mortgage_opportunity_status;
    }

    public String getOpportunity_created_at() {
        return opportunity_created_at;
    }

    @JsonProperty("opportunity_created_at")
    public void setOpportunity_created_at(String opportunity_created_at) {
        this.opportunity_created_at = opportunity_created_at;
    }

    public String getOpportunity_created_date() {
        return opportunity_created_date;
    }

    @JsonProperty("opportunity_created_date")
    public void setOpportunity_created_date(String opportunity_created_date) {
        this.opportunity_created_date = opportunity_created_date;
    }

    public String getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQuote_type() {
        return quote_type;
    }

    @JsonProperty("quote_type")
    public void setQuote_type(String quote_type) {
        this.quote_type = quote_type;
    }

    public String getQuote_type_code() {
        return quote_type_code;
    }

    @JsonProperty("quote_type_code")
    public void setQuote_type_code(String quote_type_code) {
        this.quote_type_code = quote_type_code;
    }

    public String getRequest_id() {
        return request_id;
    }

    @JsonProperty("request_id")
    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

}
