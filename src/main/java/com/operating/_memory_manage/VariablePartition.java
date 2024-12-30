package com.operating._memory_manage;

/**
 * 可变分区类
 */
public class VariablePartition {
    private String jobId;  // 作业ID
    private String allocatedSize;  // 分配的内存大小
    private String startAddress;  // 起始地址
    private String size;  // 分区的大小

    public VariablePartition(String jobId, String allocatedSize, String startAddress, String size) {
        this.jobId = jobId;
        this.allocatedSize = allocatedSize;
        this.startAddress = startAddress;
        this.size = size;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAllocatedSize() {
        return allocatedSize;
    }

    public void setAllocatedSize(String allocatedSize) {
        this.allocatedSize = allocatedSize;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

