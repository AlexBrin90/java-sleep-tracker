package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ChronotypeAnalysis implements SleepAnalysis {

    private static final LocalTime NIGHT_WINDOW_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_WINDOW_END = LocalTime.of(8, 0);

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        long owl = 0;
        long lark = 0;
        long dove = 0;

        for (var s : sessions) {
            if (!isNight(s)) { continue; };

            if (isOwl(s)) { owl++; }
            else if (isLark(s)) { lark++; }
            else { dove++; }
        }

        Chronotype result = Chronotype.DOVE;

        if (owl > lark && owl > dove) { result = Chronotype.OWL; }
        else if (lark > owl && lark > dove) { result = Chronotype.LARK; }

        return new SleepAnalysisResult("Хронотип", result);
    }

    private boolean isNight(SleepingSession s) {
        LocalDate date = s.getEnd().toLocalDate();
        LocalDateTime nightStart = LocalDateTime.of(date, NIGHT_WINDOW_START);
        LocalDateTime nightEnd = LocalDateTime.of(date, NIGHT_WINDOW_END);
        return s.getStart().isBefore(nightEnd) && s.getEnd().isAfter(nightStart);
    }

    private boolean isOwl(SleepingSession s) {
        return s.getStart().toLocalTime().isAfter(LocalTime.of(23, 0))
                && s.getEnd().toLocalTime().isAfter(LocalTime.of(9, 0));
    }

    private boolean isLark(SleepingSession s) {
        return s.getStart().toLocalTime().isBefore(LocalTime.of(22, 0))
                && s.getEnd().toLocalTime().isBefore(LocalTime.of(7, 0));
    }

}