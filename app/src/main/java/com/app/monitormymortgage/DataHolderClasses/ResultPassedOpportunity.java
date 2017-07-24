package com.app.monitormymortgage.DataHolderClasses;

/**
 * Created by Sangram on 23/06/17.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultPassedOpportunity {

    private String title;
    private String _id;
    private String mortgage_opportunity_status;
    private String mortgage_opportunity_id;
    private String owner;

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    @JsonProperty("_id")
    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMortgage_opportunity_status() {
        return mortgage_opportunity_status;
    }

    @JsonProperty("mortgage_opportunity_status")
    public void setMortgage_opportunity_status(String mortgage_opportunity_status) {
        this.mortgage_opportunity_status = mortgage_opportunity_status;
    }

    public String getMortgage_opportunity_id() {
        return mortgage_opportunity_id;
    }

    @JsonProperty("mortgage_opportunity_id")
    public void setMortgage_opportunity_id(String mortgage_opportunity_id) {
        this.mortgage_opportunity_id = mortgage_opportunity_id;
    }

    public String getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<OpportunityDataPassed> getOpportunityDataPassedList() {
        return opportunityDataPassedList;
    }

    @JsonProperty("opportunity_data")
    public void setOpportunityDataPassedList(List<OpportunityDataPassed> opportunityDataPassedList) {
        this.opportunityDataPassedList = opportunityDataPassedList;
    }

    private List<OpportunityDataPassed> opportunityDataPassedList;

}
