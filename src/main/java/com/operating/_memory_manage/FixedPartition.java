package com.operating._memory_manage;

/**
 * 固定分区类
 */
public class FixedPartition {
    private String partitionId;
    private String size;
    private String status;

    public FixedPartition(String partitionId, String size, String status) {
        this.partitionId = partitionId;
        this.size = size;
        this.status = status;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
