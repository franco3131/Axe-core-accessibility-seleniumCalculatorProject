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
import java.util.*;
import java.util.stream.Collectors;

public class AxeChecks {

  public static void check(WebDriver driver, String name, boolean failOnViolation) {
    Results results = new AxeBuilder()
        .withTags(java.util.List.of("wcag2a", "wcag2aa"))
        .analyze(driver);

    // write full JSON
    Path dir = Path.of("target","axe");
    String base = (name == null || name.isBlank()) ? "scenario" : name.replaceAll("\\W+","_");
    Path out = dir.resolve(base + ".json");

    try {
      Files.createDirectories(dir);
      var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(out.toFile(), results);
      System.out.println("[axe] wrote: " + out.toAbsolutePath());
    } catch (Exception e) {
      System.err.println("[axe] failed to write results: " + e.getMessage());
    }

    // filter actionable (env-controlled)
    List<Rule> violations = Optional.ofNullable(results.getViolations()).orElse(List.of());
    Set<String> skipRules = readCsv(System.getenv("AXE_SKIP_RULES"));
    Set<String> failImpacts = impactsAtOrAbove(System.getenv("AXE_MIN_IMPACT"));

    List<Rule> actionable = violations.stream()
        .filter(r -> !skipRules.contains(r.getId()))
        .filter(r -> r.getImpact() == null || failImpacts.contains(r.getImpact()))
        .collect(Collectors.toList());

    if (failOnViolation && !actionable.isEmpty()) {
      StringBuilder msg = new StringBuilder("Axe found ")
          .append(actionable.size()).append(" violation rules.\n");
      for (Rule r : actionable) {
        msg.append("- ").append(r.getId())
           .append(" (").append(r.getImpact()).append("): ")
           .append(r.getDescription()).append('\n');
        if (r.getHelpUrl() != null) msg.append("  ").append(r.getHelpUrl()).append('\n');
      }
      throw new AssertionError(msg.toString());
    }
  }

  private static Set<String> readCsv(String raw) {
    if (raw == null || raw.isBlank()) return Set.of();
    return Arrays.stream(raw.split(",")).map(String::trim)
        .filter(s -> !s.isEmpty()).collect(Collectors.toSet());
  }

  private static Set<String> impactsAtOrAbove(String min) {
    String m = (min == null ? "minor" : min.trim().toLowerCase());
    return switch (m) {
      case "critical" -> Set.of("critical");
      case "serious"  -> Set.of("serious","critical");
      case "moderate" -> Set.of("moderate","serious","critical");
      default         -> Set.of("minor","moderate","serious","critical");
    };
  }
}
