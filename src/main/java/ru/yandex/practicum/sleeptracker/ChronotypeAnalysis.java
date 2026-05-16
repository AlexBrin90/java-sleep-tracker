package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ChronotypeAnalysis implements SleepAnalysis<Chronotype> {

    private static final LocalTime NIGHT_WINDOW_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_WINDOW_END = LocalTime.of(6, 0);
    private static final LocalTime NIGHT_WINDOW_START_FOR_OWL = LocalTime.of(23, 0);
    private static final LocalTime NIGHT_WINDOW_END_FOR_OWL = LocalTime.of(9, 0);
    private static final LocalTime NIGHT_WINDOW_START_FOR_LARK = LocalTime.of(22, 0);
    private static final LocalTime NIGHT_WINDOW_END_FOR_LARK = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult<Chronotype> analyze(List<SleepingSession> sessions) {
        long owl = 0;
        long lark = 0;
        long dove = 0;

        for (var s : sessions) {

            if (!isNight(s)) {
                continue;
            }

            if (isOwl(s)) {
                owl++;
            } else if (isLark(s)) {
                lark++;
            } else {
                dove++;
            }
        }

        Chronotype result = Chronotype.DOVE;

        if (owl > lark && owl > dove) {
            result = Chronotype.OWL;
        } else if (lark > owl && lark > dove) {
            result = Chronotype.LARK;
        }

        return new SleepAnalysisResult<>("Хронотип", result);
    }

    private boolean isNight(SleepingSession s) {
        LocalDate date = s.getEnd().toLocalDate();
        LocalDateTime nightStart = LocalDateTime.of(date, NIGHT_WINDOW_START);
        LocalDateTime nightEnd = LocalDateTime.of(date, NIGHT_WINDOW_END);
        return s.getStart().isBefore(nightEnd) && s.getEnd().isAfter(nightStart);
    }

    private boolean isOwl(SleepingSession s) {
        return s.getStart().toLocalTime().isAfter(NIGHT_WINDOW_START_FOR_OWL)
                && s.getEnd().toLocalTime().isAfter(NIGHT_WINDOW_END_FOR_OWL);
    }

    private boolean isLark(SleepingSession s) {
        return s.getStart().toLocalTime().isBefore(NIGHT_WINDOW_START_FOR_LARK)
                && s.getEnd().toLocalTime().isBefore(NIGHT_WINDOW_END_FOR_LARK);
    }

}