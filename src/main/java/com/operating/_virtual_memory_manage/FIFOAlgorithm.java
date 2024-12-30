package com.operating._virtual_memory_manage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFOAlgorithm implements SchedulingAlgorithm {
    @Override
    public SchedulingResult execute(List<Integer> pageSequence, int capacity) {
        Queue<Integer> memory = new LinkedList<>();
        List<Integer> evictedPages = new ArrayList<>();
        List<List<Integer>> memoryStates = new ArrayList<>(); // 用于记录每一步内存状态
        int pageFaultCount = 0;

        for (int page : pageSequence) {
            if (!memory.contains(page)) {
                pageFaultCount++;
                if (memory.size() == capacity) {
                    evictedPages.add(memory.poll());
                } else {
                    evictedPages.add(-1);
                }
                memory.add(page);
            } else {
                evictedPages.add(-1);
            }

            // 记录当前内存状态
            memoryStates.add(new ArrayList<>(memory));
        }

        return new SchedulingResult(evictedPages, pageFaultCount, memoryStates);
    }

}
