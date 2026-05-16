package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageSleepDurationAnalysis implements SleepAnalysis<Long> {

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sessions) {

        long sum = sessions.stream()
                .map(SleepingSession::durationMinutes)
                .reduce(0L, Long::sum);

        long avg = (long) sessions.stream()
                .map(SleepingSession::durationMinutes)
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        return new SleepAnalysisResult<>("Средняя длительность", avg);
    }
}