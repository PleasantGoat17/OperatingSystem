package com.operating._virtual_memory_manage;

import java.util.List;

public class SchedulingResult {
    private final List<Integer> evictedPages;
    private final int pageFaultCount;
    private final List<List<Integer>> memoryStates; // 新增字段

    public SchedulingResult(List<Integer> evictedPages, int pageFaultCount, List<List<Integer>> memoryStates) {
        this.evictedPages = evictedPages;
        this.pageFaultCount = pageFaultCount;
        this.memoryStates = memoryStates;
    }

    public SchedulingResult(List<Integer> evictedPages, int pageFaultCount) {
        this.evictedPages = evictedPages;
        this.pageFaultCount = pageFaultCount;
        this.memoryStates = null;
    }

    public List<Integer> getEvictedPages() {
        return evictedPages;
    }

    public int getPageFaultCount() {
        return pageFaultCount;
    }

    public List<List<Integer>> getMemoryStates() {
        return memoryStates;
    }
}
