## Purpose
The purpose of this project is to provide a lightweight way to catch accessibility issues for UI tests that are run for my calculator UI page. 
It runs **axe-core** from a Cucumber step definition using **Selenium 4** (Page Object Model via **Page Factory**), 
When each scenario runs, oit saves one JSON report, and produces a combined **fail-only HTML** report in **GitHub Actions**.  

This is the calculator page https://rawcdn.githack.com/franco3131/Wood-Calculator-Javascript/a1175acf8a268e506e2fd6b39dbb0a6156b6c29e/Calculator/HTML/calculator.html


# Key Accessibility files 


support/ScenarioMeta.java – stores current feature/scenario names.
support/CaptureScenarioHook.java – captures names before each scenario.
Steps/AccessibilitySteps.java – step that runs axe (assert or snapshot).
accessibility/AxeChecks.java – runs axe, writes JSON, applies filters, optional fail.
accessibility/AxeFailuresFromFullReporter.java – reads all JSONs which leads to the fail-only HTML.
Driver/Setup.java – starts headless Chrome in @Before, quits in @After.



# How it runs

You call a step at the end of each scenario:

```
the page has no WCAG AA accessibility violations
```
That step runs axe on the current page and writes the JSON.

# Downloaded files genertaed:

Per scenario JSON: target/axe/<Feature>__<Scenario>.json

Combined HTML (fail-only): failures.html (built in CI, grouped by Feature)


# Tech used

Java 17

Selenium 4 (ChromeDriver)

Cucumber 7 (BDD)

axe-core Selenium Java (Deque)

WebDriverManager (auto driver binaries)

Page Object Model (POM) with Selenium Page Factory

GitHub Actions (matrix by Cucumber tag, aggregate report)

# run tests (writes JSONs to target/axe/)

mvn test

# build combined HTML from the JSONs you just produced
```
mvn -q -DskipTests -Dtest=\!Dummy test-compile
mvn -q org.codehaus.mojo:exec-maven-plugin:3.3.0:java \
  -Dexec.mainClass=com.automationCalculator.accessibility.AxeFailuresFromFullReporter \
  -Dexec.classpathScope=test \
  -Dexec.arguments="target/axe"
# open target/axe/failures.html
```



