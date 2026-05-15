package ru.yandex.practicum.sleeptracker;

import java.util.List;

public interface SleepAnalysis {
    SleepAnalysisResult analyze(List<SleepingSession> sessions);
}