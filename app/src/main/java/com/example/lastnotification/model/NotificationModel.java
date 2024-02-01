package com.example.lastnotification.model;

public class NotificationModel {

    private int id;
    private String contract_name;
    private String log_details;
    private String created_at;
    private String updated_at;

    public NotificationModel(int id, String contractName, String logDetails, String createdAt, String updatedAt) {
        this.id = id;
        this.contract_name= contractName;
        this.log_details = logDetails;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractName() {
        return contract_name;
    }

    public void setContractName(String contractName) {
        this.contract_name = contractName;
    }

    public String getLogDetails() {
        return log_details;
    }

    public void setLogDetails(String logDetails) {
        this.log_details = logDetails;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "id=" + id +
                ", contractName='" + contract_name + '\'' +
                ", logDetails='" + log_details + '\'' +
                ", createdAt='" + created_at + '\'' +
                ", updatedAt='" + updated_at + '\'' +
                '}';
    }
}

