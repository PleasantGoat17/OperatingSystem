package com.operating._disk_scheduling;

import java.util.List;

public class DiskSchedulingResult {
    private final List<Integer> serviceSequence;
    private final int totalMovement;

    public DiskSchedulingResult(List<Integer> serviceSequence, int totalMovement) {
        this.serviceSequence = serviceSequence;
        this.totalMovement = totalMovement;
    }

    public List<Integer> getServiceSequence() {
        return serviceSequence;
    }

    public int getTotalMovement() {
        return totalMovement;
    }
}
