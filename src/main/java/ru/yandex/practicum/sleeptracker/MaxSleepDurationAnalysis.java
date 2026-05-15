package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxSleepDurationAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {

        long max = sessions.stream()
                .map(SleepingSession::durationMinutes)
                .reduce((a, b) -> a > b ? a : b)
                .orElse(0L);

        return new SleepAnalysisResult("Максимальная длительность", max);
    }
}