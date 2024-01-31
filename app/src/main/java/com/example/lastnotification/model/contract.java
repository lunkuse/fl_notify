package com.example.lastnotification.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class contract {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("contract_name")
    @Expose
    private String contract_name;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("token")
    @Expose
    private  String token;
    public contract() {
    }

    public contract(int id, String name) {
        this.id = id;
        this.contract_name = contract_name;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getContractName() {
        return contract_name;
    }

    public void setContractName(String contract_name) {
        this.details = contract_name;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}