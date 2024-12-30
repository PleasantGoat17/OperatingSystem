package com.operating._disk_scheduling;

import java.util.List;

public interface DiskSchedulingAlgorithm {
    DiskSchedulingResult execute(List<Integer> trackSequence, int startPosition);
}
