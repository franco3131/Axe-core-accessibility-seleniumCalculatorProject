package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.utils.AxeReporter;   // ← add this import
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.util.List;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageName) {
        Results results = new AxeBuilder()
                .withTags(java.util.List.of("wcag2a", "wcag2aa"))
                .analyze(driver);

        // Write JSON report using AxeReporter
        Path reportPath = AxeReporter.writeResultsToJsonFile(pageName, results);

        List<Rule> violations = results.getViolations();
        if (!violations.isEmpty()) {
            StringBuilder msg = new StringBuilder("Axe found ")
                    .append(violations.size()).append(" violation rules. See: ")
                    .append(reportPath.toAbsolutePath()).append('\n');
            for (Rule r : violations) {
                msg.append("- ").append(r.getId())
                   .append(" (").append(r.getImpact()).append("): ")
                   .append(r.getDescription()).append('\n');
            }
            throw new AssertionError(msg.toString());
        }
    }
}
