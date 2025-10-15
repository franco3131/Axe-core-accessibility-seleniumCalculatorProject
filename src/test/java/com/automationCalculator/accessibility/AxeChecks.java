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

    /**
     * Runs axe on the current page, writes a pretty JSON report under target/axe/,
     * then fails if there are violations (configurable via env).
     *
     * Env toggles (optional):
     *  - AXE_SKIP_RULES: comma-separated rule ids to ignore (e.g. "document-title,html-has-lang")
     *  - AXE_MIN_IMPACT: minimum impact to fail on: minor|moderate|serious|critical (default: minor)
     */
    public static void assertNoWcagAA(WebDriver driver, String pageName) {
        // Run axe with WCAG A/AA tags (change to wcag21a/wcag21aa if you prefer)
        Results results = new AxeBuilder()
                .withTags(java.util.List.of("wcag2a", "wcag2aa"))
                .analyze(driver);

        // ---------- ALWAYS WRITE JSON FIRST ----------
        String base = (pageName == null ? "" : pageName.trim());
        if (base.isEmpty()) base = "page";                       // fallback when title is empty
        String safe = base.replaceAll("\\W+", "_");
        if (safe.isEmpty()) safe = "page";
        String fileName = safe + "-" + System.currentTimeMillis() + ".json";

        try {
            Path out = Path.of("target", "axe", fileName);
            Files.createDirectories(out.getParent());
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(out.toFile(), results);
            System.out.println("[axe] wrote report: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[axe] failed to write JSON report: " + e.getMessage());
        }
        // --------------------------------------------

        // Gather violations (can be null on some versions)
        List<Rule> violations = results.getViolations();
        if (violations == null) violations = List.of();

        // Optional filtering via environment (defaults keep all)
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
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private static Set<String> readFailImpactsFromEnv() {
        // default: "minor" => fail on all impacts (minor..critical)
        String min = System.getenv().getOrDefault("AXE_MIN_IMPACT", "minor").trim().toLowerCase();
        switch (min) {
            case "critical": return Set.of("critical");
            case "serious":  return Set.of("serious", "critical");
            case "moderate": return Set.of("moderate", "serious", "critical");
            default:         return Set.of("minor", "moderate", "serious", "critical");
        }
    }
}
