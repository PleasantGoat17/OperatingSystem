package com.operating._disk_scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCANAlgorithm implements DiskSchedulingAlgorithm {
    @Override
    public DiskSchedulingResult execute(List<Integer> trackSequence, int startPosition) {
        List<Integer> serviceSequence = new ArrayList<>();
        int totalMovement = 0;
        int currentPosition = startPosition;

        // SCAN算法的处理：先向一个方向（如右）移动
        List<Integer> sortedTracks = new ArrayList<>(trackSequence);
        Collections.sort(sortedTracks);

        // 定义两个集合：一个向左，一个向右
        List<Integer> leftTracks = new ArrayList<>();
        List<Integer> rightTracks = new ArrayList<>();

        for (int track : sortedTracks) {
            if (track < startPosition) {
                leftTracks.add(track);
            } else {
                rightTracks.add(track);
            }
        }

        // 按顺序服务左右磁道
        // 向右扫
        for (int track : rightTracks) {
            totalMovement += Math.abs(track - currentPosition);
            serviceSequence.add(track);
            currentPosition = track;
        }

        // 向左扫
        for (int i = leftTracks.size() - 1; i >= 0; i--) {
            totalMovement += Math.abs(leftTracks.get(i) - currentPosition);
            serviceSequence.add(leftTracks.get(i));
            currentPosition = leftTracks.get(i);
        }

        return new DiskSchedulingResult(serviceSequence, totalMovement);
    }
}
