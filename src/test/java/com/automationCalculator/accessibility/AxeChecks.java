package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageName) {
        Results results = new AxeBuilder()
                .withTags(java.util.List.of("wcag2a", "wcag2aa")) // or wcag21a/wcag21aa
                .analyze(driver);

        // Write pretty JSON report to target/axe/<pageName>.json
        try {
            Path out = Path.of("target", "axe", pageName.replaceAll("\\W+", "_") + ".json");
            Files.createDirectories(out.getParent());
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(out.toFile(), results);
            System.out.println("[axe] wrote report: " + out.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[axe] failed to write JSON report: " + e.getMessage());
        }

        // Fail the test if there are violations
        List<Rule> violations = results.getViolations();
        if (!violations.isEmpty()) {
            StringBuilder msg = new StringBuilder("Axe found ")
                    .append(violations.size()).append(" violation rules.\n");
            for (Rule r : violations) {
                msg.append("- ").append(r.getId())
                   .append(" (").append(r.getImpact()).append("): ")
                   .append(r.getDescription()).append('\n');
            }
            throw new AssertionError(msg.toString());
        }
    }
}
