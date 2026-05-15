package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class SessionCountAnalysis implements SleepAnalysis {

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        return new SleepAnalysisResult(
                "Количество сессий",
                sessions.size()
        );
    }
}