package com.operating._memory_manage;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存管理器，包含分区分配和释放逻辑
 */
public class MemoryManager {
    private final List<FixedPartition> fixedPartitions;        // 固定分区列表
    private final List<VariablePartition> allocatedPartitions; // 已分配的可变分区列表
    private final List<VariablePartition> unallocatedPartitions; // 未分配的可变分区列表
    private final List<Job> jobs;                              // 作业列表

    public MemoryManager() {
        this.fixedPartitions = new ArrayList<>();
        this.allocatedPartitions = new ArrayList<>();
        this.unallocatedPartitions = new ArrayList<>();
        this.jobs = new ArrayList<>();

        // 初始化固定分区和未分配分区
        initializeFixedPartitions();
        initializeUnallocatedPartitions();
    }

    private void initializeFixedPartitions() {
        fixedPartitions.add(new FixedPartition("1", "32K", "空闲"));
        fixedPartitions.add(new FixedPartition("2", "32K", "空闲"));
        fixedPartitions.add(new FixedPartition("3", "32K", "空闲"));
        fixedPartitions.add(new FixedPartition("4", "28K", "空闲"));
    }

    private void initializeUnallocatedPartitions() {
        unallocatedPartitions.add(new VariablePartition("", "", "4K", "124K"));
    }

    public List<FixedPartition> getFixedPartitions() {
        return fixedPartitions;
    }

    public List<VariablePartition> getAllocatedPartitions() {
        return allocatedPartitions;
    }

    public List<VariablePartition> getUnallocatedPartitions() {
        return unallocatedPartitions;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    // 新增作业到作业列表
    private boolean addJob(Job job) {
        for (Job existingJob : jobs) {
            if (existingJob.getJobId().equals(job.getJobId())) {
                // 如果作业ID已存在，分配失败
                return false;
            }
        }
        jobs.add(job);
        return true;
    }

    // 删除作业
    private void removeJob(String jobId) {
        jobs.removeIf(job -> job.getJobId().equals(jobId));
    }

    // 固定分区的分配逻辑
    public boolean allocateFixedPartition(String partitionId, String jobId, int jobSize) {
        Job job = new Job(jobId, jobSize);
        if (!addJob(job)) {
            return false; // 作业已存在，分配失败
        }

        for (FixedPartition partition : fixedPartitions) {
            if (partition.getPartitionId().equals(partitionId) && "空闲".equals(partition.getStatus())) {
                int partitionSize = Integer.parseInt(partition.getSize().replace("K", "").trim());
                if (partitionSize >= jobSize) {
                    partition.setStatus("已分配给作业：" + jobId);
                    return true;
                } else {
                    return false; // 分区大小不足
                }
            }
        }
        removeJob(jobId); // 分配失败，移除作业
        return false;
    }

    // 固定分区的释放逻辑
    public boolean releaseFixedPartition(String partitionId) {
        for (FixedPartition partition : fixedPartitions) {
            if (partition.getPartitionId().equals(partitionId)) {
                if (!"空闲".equals(partition.getStatus())) {
                    // 获取分配的作业ID并移除作业
                    String jobId = partition.getStatus().replace("已分配给作业：", "");
                    removeJob(jobId);
                    partition.setStatus("空闲");
                    return true;
                }
            }
        }
        return false;
    }

    // 可变分区的分配逻辑
    public boolean allocateVariablePartition(String jobId, int size) {
        Job job = new Job(jobId, size);
        if (!addJob(job)) {
            return false; // 作业已存在，分配失败
        }

        for (VariablePartition partition : unallocatedPartitions) {
            int partitionSize = Integer.parseInt(partition.getSize().replace("K", "").trim());
            if (partitionSize >= size) {
                int startAddress = Integer.parseInt(partition.getStartAddress().replace("K", "").trim());
                VariablePartition allocatedPartition = new VariablePartition(jobId, size + "K", startAddress + "K", size + "K");
                allocatedPartitions.add(allocatedPartition);

                // 更新未分配分区
                int newSize = partitionSize - size;
                partition.setStartAddress((startAddress + size) + "K");
                partition.setSize(newSize > 0 ? newSize + "K" : "0K");

                if (newSize == 0) {
                    unallocatedPartitions.remove(partition);
                }
                return true;
            }
        }
        removeJob(jobId); // 分配失败，移除作业
        return false;
    }

    // 可变分区的释放逻辑
    public boolean releaseVariablePartition(String jobId) {
        for (VariablePartition partition : allocatedPartitions) {
            if (partition.getJobId().equals(jobId)) {
                allocatedPartitions.remove(partition);
                unallocatedPartitions.add(partition); // 回收至未分配分区
                removeJob(jobId);
                return true;
            }
        }
        return false;
    }

    // 释放作业的内存
    public boolean releaseMemory(String jobId) {
        boolean success = releaseVariablePartition(jobId);
        if (!success) {
            for (FixedPartition partition : fixedPartitions) {
                if (partition.getStatus().contains(jobId)) {
                    success = releaseFixedPartition(partition.getPartitionId());
                    break;
                }
            }
        }
        return success;
    }

    public List<String> getPartitionIds(PartitionType partitionType) {
        List<String> partitionIds = new ArrayList<>();

        if (partitionType == PartitionType.FIXED) {
            // 返回所有固定分区的ID
            for (FixedPartition partition : fixedPartitions) {
                partitionIds.add(partition.getPartitionId());
            }
        } else if (partitionType == PartitionType.VARIABLE) {
            // 返回所有可变分区的ID
            for (VariablePartition partition : unallocatedPartitions) {
                partitionIds.add(partition.getStartAddress() + " (" + partition.getSize() + ")");
            }
        }

        return partitionIds;
    }

    public int getJobSize() {
        return jobs.size();
    }

    // 在MemoryManager类中添加getAllJobIds方法
    public List<String> getAllJobIds() {
        List<String> jobIds = new ArrayList<>();
        for (Job job : jobs) {
            jobIds.add(job.getJobId()); // 获取每个作业的ID并添加到列表中
        }
        return jobIds;
    }
}
