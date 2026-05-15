package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageSleepDurationAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {

        long sum = sessions.stream()
                .map(SleepingSession::durationMinutes)
                .reduce(0L, Long::sum);

        long avg;

        if (sessions.isEmpty()) {
            avg = 0;
        } else {
            avg = sum / sessions.size();
        }

        return new SleepAnalysisResult("Средняя длительность", avg);
    }
}