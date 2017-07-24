package com.app.monitormymortgage.DataHolderClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Sangram on 23/06/17.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class OpportunityDataPassed
{

    private String _id;
    private String mortgage_id;
    private String type;
    private String type_code;
    private String created_at;
    private String status_updated_at;

    public String get_id() {
        return _id;
    }

    @JsonProperty("_id")
    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMortgage_id() {
        return mortgage_id;
    }

    @JsonProperty("mortgage_id")
    public void setMortgage_id(String mortgage_id) {
        this.mortgage_id = mortgage_id;
    }

    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public String getType_code() {
        return type_code;
    }

    @JsonProperty("type_code")
    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus_updated_at() {
        return status_updated_at;
    }

    @JsonProperty("status_updated_at")
    public void setStatus_updated_at(String status_updated_at) {
        this.status_updated_at = status_updated_at;
    }
}
