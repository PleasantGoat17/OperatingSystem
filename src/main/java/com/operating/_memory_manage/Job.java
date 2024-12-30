package com.operating._memory_manage;

/**
 * 作业类
 */
public class Job {
    private String jobId;  // 作业ID
    private int size;      // 所需内存大小（单位：K）

    public Job(String jobId, int size) {
        this.jobId = jobId;
        this.size = size;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
