import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AxeChecks {

    public static void assertNoWcagAA(WebDriver driver, String pageName) {
        Results results = new AxeBuilder()
                .withTags(List.of("wcag2a", "wcag2aa"))   // or Arrays.asList(...) on Java 8
                // .include("main")     // optional: scope
                // .exclude("#cookie")  // optional: ignore area
                .analyze(driver);

        List<Rule> violations = results.getViolations();

        try {
            Path out = Path.of("target", "axe", pageName + ".json");
            Files.createDirectories(out.getParent());
            Files.writeString(out, results.toJson());
        } catch (Exception ignore) {}

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
