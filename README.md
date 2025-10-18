


**Key added accessibility files **

support/ScenarioMeta.java – stores current feature/scenario names.

support/CaptureScenarioHook.java – captures names before each scenario.

Steps/AccessibilitySteps.java – step(s) that run axe (assert or snapshot).

accessibility/AxeChecks.java – runs axe, writes JSON, applies filters, optional fail.

accessibility/AxeFailuresFromFullReporter.java – reads all JSONs → fail-only HTML.

Driver/Setup.java – starts headless Chrome in @Before, quits in @After.





**How it runs**

You call a step at the end of each scenario:

Then the page has no WCAG AA accessibility violations


That step runs axe on the current page and writes the JSON.
It fails the scenario if violations meet your criteria.


**Downloaded files:**

Per scenario JSON: target/axe/<Feature>__<Scenario>.json

Combined HTML (fail-only): failures.html (built in CI, grouped by Feature)


Tech used

Java 17

Selenium 4 (ChromeDriver)

Cucumber 7 (BDD)

axe-core Selenium Java (Deque)

WebDriverManager (auto driver binaries)

Page Object Model (POM) with Selenium Page Factory

GitHub Actions (matrix by Cucumber tag, aggregate report)
