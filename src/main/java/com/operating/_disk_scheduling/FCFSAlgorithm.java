package com.operating._disk_scheduling;

import java.util.ArrayList;
import java.util.List;

public class FCFSAlgorithm implements DiskSchedulingAlgorithm {
    @Override
    public DiskSchedulingResult execute(List<Integer> trackSequence, int startPosition) {
        int totalMovement = 0;
        int currentPosition = startPosition;
        List<Integer> serviceSequence = new ArrayList<>();

        for (int track : trackSequence) {
            totalMovement += Math.abs(track - currentPosition);
            serviceSequence.add(track);
            currentPosition = track;
        }

        return new DiskSchedulingResult(serviceSequence, totalMovement);
    }
}
