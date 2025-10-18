// src/test/java/com/automationCalculator/support/CaptureScenarioHook.java
package com.automationCalculator.support;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.net.URI;
import java.nio.file.Paths;

public class CaptureScenarioHook {
  @Before(order = 0)
  public void capture(Scenario s) {
    ScenarioMeta.feature.set(deriveFeatureName(s));
    ScenarioMeta.scenario.set(s.getName());
  }

  private static String deriveFeatureName(Scenario s) {
    // Prefer Scenario.getUri() (Cucumber 6/7+)
    try {
      URI uri = s.getUri();
      if (uri != null) {
        String fn = Paths.get(uri).getFileName().toString();
        return fn.replaceFirst("\\.feature$", "");
      }
    } catch (Throwable ignored) {}

    // Fallback: parse Scenario.getId() like ".../Addition.feature:12"
    try {
      String id = s.getId();
      String path = id.split(":")[0];
      String fn = path.substring(path.replace('\\','/').lastIndexOf('/') + 1);
      return fn.replaceFirst("\\.feature$", "");
    } catch (Throwable ignored) {}

    return "Feature";
  }
}
