package com.operating._virtual_memory_manage;

import java.util.List;

public interface SchedulingAlgorithm {
    // 执行页面调度算法
    SchedulingResult execute(List<Integer> pageSequence, int capacity);
}
