// src/test/java/com/automationCalculator/accessibility/AxeChecks.java
package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageOrScenarioName) {
        Results results = new AxeBuilder()
                .withTags(java.util.List.of("wcag2a", "wcag2aa"))
                .analyze(driver);

        // ---------- WRITE JSON FIRST ----------
        String base = pageOrScenarioName == null ? "" : pageOrScenarioName.trim();
        if (base.isEmpty()) base = "page";
        String safe = base.replaceAll("\\W+", "_");
        if (safe.isEmpty()) safe = "page";

        // avoid overwriting: add -1, -2, … if needed (no timestamp)
        Path dir = Path.of("target", "axe");
        Path out = dir.resolve(safe + ".json");
        int i = 1;
        try {
            Files.createDirectories(dir);
            while (Files.exists(out)) {
                out = dir.resolve(safe + "-" + i + ".json");
                i++;
            }
            var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(out.toFile(), results);
            System.out.println("[axe] wrote report: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[axe] failed to write JSON report: " + e.getMessage());
        }
        // -------------------------------------

        // Optional filtering via env vars
        List<Rule> violations = results.getViolations();
        if (violations == null) violations = List.of();

        Set<String> skipRules = readSkipRulesFromEnv();
        Set<String> failImpacts = readFailImpactsFromEnv();

        List<Rule> actionable = violations.stream()
                .filter(r -> !skipRules.contains(r.getId()))
                .filter(r -> r.getImpact() == null || failImpacts.contains(r.getImpact()))
                .collect(Collectors.toList());

        if (!actionable.isEmpty()) {
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

    private static Set<String> readSkipRulesFromEnv() {
        String raw = System.getenv().getOrDefault("AXE_SKIP_RULES", "").trim();
        if (raw.isEmpty()) return Set.of();
        return Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private static Set<String> readFailImpactsFromEnv() {
        String min = System.getenv().getOrDefault("AXE_MIN_IMPACT", "minor").trim().toLowerCase();
        switch (min) {
            case "critical": return Set.of("critical");
            case "serious":  return Set.of("serious", "critical");
            case "moderate": return Set.of("moderate", "serious", "critical");
            default:         return Set.of("minor", "moderate", "serious", "critical");
        }
    }
}
