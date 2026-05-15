package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class SleeplessNightsAnalysis implements SleepAnalysis {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи", 0L);
        }

        LocalDate firstNight = getNightDate(sessions.stream()
                .min(Comparator.comparing(SleepingSession::getStart))
                .orElseThrow().getStart());

        LocalDate lastNight = getNightDate(sessions.stream()
                .max(Comparator.comparing(SleepingSession::getEnd))
                .orElseThrow().getEnd());

        long sleepless = firstNight.datesUntil(lastNight.plusDays(1))
                //!!! datesUntil - метод вызывает даты от начальной до конечной (не включильно) (то есть от firstNight до lastNight включительно)
                .filter(nightDate -> {
                    LocalDateTime windowStart = LocalDateTime.of(nightDate, NIGHT_START);
                    LocalDateTime windowEnd = LocalDateTime.of(nightDate, NIGHT_END);

                    return !sessions.stream()
                            .anyMatch(s -> intersects(s.getStart(), s.getEnd(), windowStart, windowEnd));
                })
                .count();

        return new SleepAnalysisResult("Бессонные ночи", sleepless);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        return dateTime.toLocalTime().isBefore(LocalTime.NOON) ? date : date.plusDays(1);
    }


    private boolean intersects(LocalDateTime s1, LocalDateTime e1, LocalDateTime s2, LocalDateTime e2) {
        return s1.isBefore(e2) && e1.isAfter(s2);
    }
}