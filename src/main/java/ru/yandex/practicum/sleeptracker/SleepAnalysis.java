package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
public interface SleepAnalysis<T> {
    SleepAnalysisResult<T> analyze(List<SleepingSession> sessions);
}