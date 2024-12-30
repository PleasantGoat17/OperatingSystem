package com.operating._virtual_memory_manage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class LRUAlgorithm implements SchedulingAlgorithm {
    @Override
    public SchedulingResult execute(List<Integer> pageSequence, int capacity) {
        LinkedHashSet<Integer> memory = new LinkedHashSet<>();
        List<Integer> evictedPages = new ArrayList<>();
        List<List<Integer>> memoryStates = new ArrayList<>(); // 用于记录每一步内存状态
        int pageFaultCount = 0;

        for (int page : pageSequence) {
            if (!memory.contains(page)) {
                pageFaultCount++;
                if (memory.size() == capacity) {
                    int first = memory.iterator().next();
                    memory.remove(first);
                    evictedPages.add(first);
                } else {
                    evictedPages.add(-1);
                }
            } else {
                memory.remove(page); // 重新添加到队尾
                evictedPages.add(-1);
            }
            memory.add(page);

            // 记录当前内存状态
            memoryStates.add(new ArrayList<>(memory));
        }

        return new SchedulingResult(evictedPages, pageFaultCount, memoryStates);
    }
}
