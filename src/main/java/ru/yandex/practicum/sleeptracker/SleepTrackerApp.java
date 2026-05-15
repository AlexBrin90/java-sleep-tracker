package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SleepTrackerApp {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) throws IOException {
        System.out.print("Введите путь до файла: ");
        String pathToFile = new Scanner(System.in).nextLine().trim();

        if (pathToFile.isEmpty()) {
            System.out.println("Путь пуст");
            return;
        }

        Path path = Path.of(pathToFile);

        List<SleepingSession> sessions = Files.lines(path)
                .filter(line -> !line.isBlank())
                .map(SleepTrackerApp::parse)
                .toList();

        List<SleepAnalysis> analyses = List.of(
                new SessionCountAnalysis(),
                new MinSleepDurationAnalysis(),
                new MaxSleepDurationAnalysis(),
                new AverageSleepDurationAnalysis(),
                new BadSleepCountAnalysis(),
                new SleeplessNightsAnalysis(),
                new ChronotypeAnalysis()
        );

        analyses.stream()
                .map(analysis -> analysis.analyze(sessions))
                .forEach(result ->
                        System.out.println(result.getDescription() + ": " + result.getValue())
                );
    }

    private static SleepingSession parse(String line) {
        String[] p = line.split(";");

        return new SleepingSession(
                LocalDateTime.parse(p[0].trim(), FORMATTER),
                LocalDateTime.parse(p[1].trim(), FORMATTER),
                SleepQuality.valueOf(p[2].trim().toUpperCase())
        );
    }
}