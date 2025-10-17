package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageOrScenarioName) {
        Results results = new AxeBuilder()
                .withTags(java.util.List.of("wcag2a", "wcag2aa"))
                .analyze(driver);

        // ---------- write full results ----------
        String base = (pageOrScenarioName == null || pageOrScenarioName.trim().isEmpty())
                ? "page" : pageOrScenarioName.trim().replaceAll("\\W+", "_");
        Path dir = Path.of("target", "axe");
        Path full = dir.resolve(base + ".json");

        try {
            Files.createDirectories(dir);
            var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(full.toFile(), results);
            System.out.println("[axe] wrote full: " + full.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[axe] failed to write full results: " + e.getMessage());
        }

        // ---------- filter to actionable (what we actually fail on) ----------
        List<Rule> violations = Optional.ofNullable(results.getViolations()).orElse(List.of());
        Set<String> skipRules = readCsv(System.getenv("AXE_SKIP_RULES"));
        Set<String> failImpacts = impactsAtOrAbove(System.getenv("AXE_MIN_IMPACT")); // default=minor

        List<Rule> actionable = violations.stream()
                .filter(r -> !skipRules.contains(r.getId()))
                .filter(r -> r.getImpact() == null || failImpacts.contains(r.getImpact()))
                .collect(Collectors.toList());

        // ---------- if failing, write a FAIL- summary file ----------
        if (!actionable.isEmpty()) {
            record FailRule(String id, String impact, String description, String helpUrl) {}
            List<FailRule> failOnly = actionable.stream()
                    .map(r -> new FailRule(r.getId(),
                            r.getImpact(),
                            r.getDescription(),
                            r.getHelpUrl()))
                    .collect(Collectors.toList());

            try {
                var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
                Path fail = dir.resolve("FAIL-" + base + ".json");
                mapper.writeValue(fail.toFile(), failOnly);
                System.out.println("[axe] wrote fail-only: " + fail.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("[axe] failed to write fail-only: " + e.getMessage());
            }

            // also print to console, then fail
            StringBuilder msg = new StringBuilder("Axe found ")
                    .append(actionable.size()).append(" violation rules.\n");
            for (Rule r : actionable) {
                msg.append("- ").append(r.getId())
                   .append(" (").append(r.getImpact()).append("): ")
                   .append(r.getDescription()).append('\n');
            }
            throw new AssertionError(msg.toString());
        }
    }

    private static Set<String> readCsv(String raw) {
        if (raw == null || raw.isBlank()) return Set.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private static Set<String> impactsAtOrAbove(String min) {
        String m = (min == null ? "minor" : min.trim().toLowerCase());
        return switch (m) {
            case "critical" -> Set.of("critical");
            case "serious"  -> Set.of("serious", "critical");
            case "moderate" -> Set.of("moderate", "serious", "critical");
            default         -> Set.of("minor", "moderate", "serious", "critical");
        };
    }
}
