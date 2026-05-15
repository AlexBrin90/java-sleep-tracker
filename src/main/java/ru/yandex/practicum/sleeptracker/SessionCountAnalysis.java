package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class SessionCountAnalysis implements SleepAnalysis<Integer> {

    @Override
    public SleepAnalysisResult<Integer> analyze(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>(
                "Количество сессий",
                sessions.size()
        );
    }
}