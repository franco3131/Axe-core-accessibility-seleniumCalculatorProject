package com.automationCalculator.support;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CaptureScenarioHook {
  @Before(order = 0)
  public void capture(Scenario s) {
    ScenarioName.current.set(s.getName());
  }
}
