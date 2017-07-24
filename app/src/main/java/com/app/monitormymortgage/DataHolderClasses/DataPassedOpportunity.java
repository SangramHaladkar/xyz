package com.app.monitormymortgage.DataHolderClasses;

/**
 * Created by Sangram on 23/06/17.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataPassedOpportunity {

    private String code;

    private String message;

    private List<ResultPassedOpportunity> resultList;


    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultPassedOpportunity> getResultList() {
        return resultList;
    }

    @JsonProperty("result")
    public void setResultList(List<ResultPassedOpportunity> resultList) {
        this.resultList = resultList;
    }

}
