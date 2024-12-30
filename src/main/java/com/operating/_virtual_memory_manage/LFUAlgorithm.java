package com.operating._virtual_memory_manage;

import java.util.*;

public class LFUAlgorithm implements SchedulingAlgorithm {
    @Override
    public SchedulingResult execute(List<Integer> pageSequence, int capacity) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        List<Integer> memory = new ArrayList<>();
        List<Integer> evictedPages = new ArrayList<>();
        List<List<Integer>> memoryStates = new ArrayList<>(); // 用于记录每一步内存状态
        int pageFaultCount = 0;

        for (int page : pageSequence) {
            frequencyMap.put(page, frequencyMap.getOrDefault(page, 0) + 1);

            if (!memory.contains(page)) {
                pageFaultCount++;
                if (memory.size() == capacity) {
                    // 淘汰频率最小的页面
                    int leastFrequentPage = memory.stream()
                            .min(Comparator.comparingInt(frequencyMap::get))
                            .orElseThrow();
                    memory.remove(Integer.valueOf(leastFrequentPage));
                    evictedPages.add(leastFrequentPage);
                } else {
                    evictedPages.add(-1);
                }
                memory.add(page);
            } else {
                evictedPages.add(-1); // 未发生淘汰
            }

            // 记录当前内存状态
            memoryStates.add(new ArrayList<>(memory));
        }

        return new SchedulingResult(evictedPages, pageFaultCount, memoryStates);
    }
}
