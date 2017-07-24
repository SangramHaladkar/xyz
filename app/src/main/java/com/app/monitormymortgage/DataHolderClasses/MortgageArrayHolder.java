package com.app.monitormymortgage.DataHolderClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Sangram on 28/06/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MortgageArrayHolder {

    private String mortgage_id;

    private String title;

    private String mortgage_opportunity_status;

    private String owner;

    private String opportunity_created_at;

    private String lender_id;

    private String lender_name;

    private String lender_logo;

    public String getMortgage_id() {
        return mortgage_id;
    }

    @JsonProperty("mortgage_id")
    public void setMortgage_id(String mortgage_id) {
        this.mortgage_id = mortgage_id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMortgage_opportunity_status() {
        return mortgage_opportunity_status;
    }

    @JsonProperty("mortgage_opportunity_status")
    public void setMortgage_opportunity_status(String mortgage_opportunity_status) {
        this.mortgage_opportunity_status = mortgage_opportunity_status;
    }

    public String getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getOpportunity_created_at() {
        return opportunity_created_at;
    }

    @JsonProperty("opportunity_created_at")
    public void setOpportunity_created_at(String opportunity_created_at) {
        this.opportunity_created_at = opportunity_created_at;
    }

    public String getLender_id() {
        return lender_id;
    }

    @JsonProperty("lender_id")
    public void setLender_id(String lender_id) {
        this.lender_id = lender_id;
    }

    public String getLender_name() {
        return lender_name;
    }

    @JsonProperty("lender_name")
    public void setLender_name(String lender_name) {
        this.lender_name = lender_name;
    }

    public String getLender_logo() {
        return lender_logo;
    }

    @JsonProperty("lender_logo")
    public void setLender_logo(String lender_logo) {
        this.lender_logo = lender_logo;
    }
}
