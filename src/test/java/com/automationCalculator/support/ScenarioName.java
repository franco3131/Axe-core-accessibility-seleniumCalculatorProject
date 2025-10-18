package com.automationCalculator.support;

public class ScenarioName {
  public static final ThreadLocal<String> current = new ThreadLocal<>();
  public static String safe() {
    String n = current.get();
    if (n == null || n.isBlank()) return "scenario";
    return n.replaceAll("\\W+", "_");
  }
}
