// src/test/java/com/automationCalculator/support/ScenarioMeta.java
package com.automationCalculator.support;

public final class ScenarioMeta {
  private ScenarioMeta(){}
  public static final ThreadLocal<String> feature = new ThreadLocal<>();
  public static final ThreadLocal<String> scenario = new ThreadLocal<>();

  public static String safeFeature() {
    String f = feature.get();
    if (f == null || f.isBlank()) return "Feature";
    return f.replaceAll("\\W+","_");
  }

  public static String safeScenario() {
    String s = scenario.get();
    if (s == null || s.isBlank()) return "Scenario";
    return s.replaceAll("\\W+","_");
  }

  /** e.g., Addition__Multiply_two_numbers */
  public static String label() { return safeFeature() + "__" + safeScenario(); }
}
