// src/test/java/com/automationcalculator/accessibility/AxeChecks.java
package com.automationcalculator.accessibility;

import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageName) {
        Results results = new AxeBuilder()
            .withTags("wcag2a", "wcag2aa")      // scope to WCAG A/AA
            //.include("main")                  // optionally limit scope
            //.exclude("#consent-modal")        // optionally ignore areas
            .analyze(driver);

        List<Rule> violations = results.getViolations();

        // Save a JSON report per page under target/axe/
        try {
            Path out = Path.of("target", "axe", pageName + ".json");
            Files.createDirectories(out.getParent());
            Files.writeString(out, results.toJson());
        } catch (Exception ignored) {}

        if (!violations.isEmpty()) {
            StringBuilder msg = new StringBuilder("Axe found ")
                .append(violations.size()).append(" violation rules. See target/axe/")
                .append(pageName).append(".json\n");

            for (Rule r : violations) {
                msg.append("- ").append(r.getId())
                   .append(" (").append(r.getImpact()).append("): ")
                   .append(r.getDescription()).append('\n');
            }
            throw new AssertionError(msg.toString());
        }
    }
}
