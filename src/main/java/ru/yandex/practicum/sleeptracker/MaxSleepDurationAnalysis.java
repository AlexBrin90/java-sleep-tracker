package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxSleepDurationAnalysis implements SleepAnalysis<Long> {

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sessions) {

        long max = sessions.stream()
                .map(SleepingSession::durationMinutes)
                .reduce((a, b) -> a > b ? a : b)
                .orElse(0L);

        return new SleepAnalysisResult<>("Максимальная длительность", max);
    }
}