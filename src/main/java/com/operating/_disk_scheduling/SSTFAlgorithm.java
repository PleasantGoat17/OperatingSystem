package com.operating._disk_scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SSTFAlgorithm implements DiskSchedulingAlgorithm {
    @Override
    public DiskSchedulingResult execute(List<Integer> trackSequence, int startPosition) {
        List<Integer> serviceSequence = new ArrayList<>();
        int totalMovement = 0;
        int currentPosition = startPosition;

        while (!trackSequence.isEmpty()) {
            // 找到距离当前位置最近的磁道
            final int position = currentPosition; // 创建一个有效final的局部变量
            int closestTrack = trackSequence.stream()
                    .min(Comparator.comparingInt(track -> Math.abs(track - position))) // 使用final变量
                    .orElseThrow();

            totalMovement += Math.abs(closestTrack - currentPosition);
            serviceSequence.add(closestTrack);
            trackSequence.remove(Integer.valueOf(closestTrack)); // 从列表中移除已服务的磁道
            currentPosition = closestTrack;
        }

        return new DiskSchedulingResult(serviceSequence, totalMovement);
    }
}
