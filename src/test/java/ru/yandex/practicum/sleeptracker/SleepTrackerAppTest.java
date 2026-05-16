package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    private SleepingSession session(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        return new SleepingSession(start, end, quality);
    }

    private LocalDateTime ldt(int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Test
    void sessionCount_empty_returnsZero() {
        var analysis = new SessionCountAnalysis();
        assertEquals(0, (int) analysis.analyze(List.of()).getValue());
    }

    @Test
    void sessionCount_multiple_returnsCorrectCount() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 22, 0), ldt(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 14, 0), ldt(2025, 10, 2, 15, 0), SleepQuality.NORMAL)
        );
        assertEquals(2, (int) new SessionCountAnalysis().analyze(sessions).getValue());
    }

    @Test
    void minDuration_mixed_returnsMinimum() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 23, 0), ldt(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 14, 0), ldt(2025, 10, 2, 14, 45), SleepQuality.NORMAL)
        );
        assertEquals(45L, (long) new MinSleepDurationAnalysis().analyze(sessions).getValue());
    }

    @Test
    void maxDuration_mixed_returnsMaximum() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 22, 0), ldt(2025, 10, 2, 5, 30), SleepQuality.NORMAL),
                session(ldt(2025, 10, 2, 23, 0), ldt(2025, 10, 3, 8, 0), SleepQuality.GOOD)
        );
        assertEquals(540L, (long) new MaxSleepDurationAnalysis().analyze(sessions).getValue());
    }

    @Test
    void averageDuration_integerDivision() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 0, 0), ldt(2025, 10, 1, 0, 10), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 0, 0), ldt(2025, 10, 2, 0, 11), SleepQuality.GOOD)
        );
        assertEquals(10L, (long) new AverageSleepDurationAnalysis().analyze(sessions).getValue());
    }

    @Test
    void badSleepCount_filtersOnlyBad() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 22, 0), ldt(2025, 10, 2, 6, 0), SleepQuality.BAD),
                session(ldt(2025, 10, 2, 22, 0), ldt(2025, 10, 3, 6, 0), SleepQuality.NORMAL),
                session(ldt(2025, 10, 3, 22, 0), ldt(2025, 10, 4, 6, 0), SleepQuality.GOOD)
        );
        assertEquals(1L, (long) new BadSleepCountAnalysis().analyze(sessions).getValue());
    }

    @Test
    void sleeplessNights_oneMissingNight_returnsOne() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 23, 0), ldt(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                session(ldt(2025, 10, 3, 23, 0), ldt(2025, 10, 4, 6, 0), SleepQuality.GOOD)
        );
        assertEquals(1L, (long) new SleeplessNightsAnalysis().analyze(sessions).getValue());
    }

    @Test
    void sleeplessNights_crossesMidnight_notSleepless() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 2, 0), ldt(2025, 10, 1, 7, 0), SleepQuality.NORMAL),
                session(ldt(2025, 10, 2, 5, 30), ldt(2025, 10, 2, 9, 0), SleepQuality.NORMAL)
        );
        assertEquals(0L, (long) new SleeplessNightsAnalysis().analyze(sessions).getValue());
    }

    @Test
    void sleeplessNights_onlyDaySleep_allNightsSleepless() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 14, 0), ldt(2025, 10, 1, 15, 0), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 14, 0), ldt(2025, 10, 2, 15, 0), SleepQuality.GOOD)
        );
        assertEquals(2L, (long) new SleeplessNightsAnalysis().analyze(sessions).getValue());
    }

    @Test
    void sleeplessNights_monthBoundary_correctCount() {
        var sessions = List.of(
                session(ldt(2025, 10, 31, 23, 0), ldt(2025, 11, 1, 7, 0), SleepQuality.GOOD),
                session(ldt(2025, 11, 2, 23, 0), ldt(2025, 11, 3, 7, 0), SleepQuality.GOOD)
        );
        assertEquals(1L, (long) new SleeplessNightsAnalysis().analyze(sessions).getValue());
    }

    @Test
    void chronotype_owlDominant_returnsOwl() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 23, 30), ldt(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 23, 15), ldt(2025, 10, 3, 9, 45), SleepQuality.GOOD),
                session(ldt(2025, 10, 3, 21, 0), ldt(2025, 10, 4, 6, 0), SleepQuality.GOOD)
        );
        assertEquals(Chronotype.OWL, new ChronotypeAnalysis().analyze(sessions).getValue());
    }

    @Test
    void chronotype_tie_returnsDove() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 23, 30), ldt(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 21, 0), ldt(2025, 10, 3, 6, 0), SleepQuality.GOOD)
        );
        assertEquals(Chronotype.DOVE, new ChronotypeAnalysis().analyze(sessions).getValue());
    }

    @Test
    void chronotype_onlyDaySessions_returnsDove() {
        var sessions = List.of(
                session(ldt(2025, 10, 1, 14, 0), ldt(2025, 10, 1, 15, 0), SleepQuality.GOOD),
                session(ldt(2025, 10, 2, 13, 0), ldt(2025, 10, 2, 14, 0), SleepQuality.GOOD)
        );
        assertEquals(Chronotype.DOVE, new ChronotypeAnalysis().analyze(sessions).getValue());
    }
}