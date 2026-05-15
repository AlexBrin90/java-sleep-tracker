package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadSleepCountAnalysis implements SleepAnalysis<Long> {

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sessions) {

        long count = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Плохие сессии", count);
    }
}