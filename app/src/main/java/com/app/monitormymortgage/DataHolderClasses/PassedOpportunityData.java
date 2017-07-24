package com.app.monitormymortgage.DataHolderClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Sangram on 21/06/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassedOpportunityData {


    private String status;

    private DataPassedOpportunity data;

    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public DataPassedOpportunity getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(DataPassedOpportunity data) {
        this.data = data;
    }


}
