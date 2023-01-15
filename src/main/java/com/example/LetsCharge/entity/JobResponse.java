package com.example.LetsCharge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class JobResponse {
    @Id
    @Column(name = "job_id", nullable = false)
    private String jobId;

    private boolean success;
    private String jobGroup;
    private String message;

    public JobResponse() {
    }

    public JobResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public JobResponse(boolean success, String jobid, String jobGroup, String message) {
        this.jobId = jobId;
        this.success = success;
        this.jobGroup = jobGroup;
        this.message = message;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
