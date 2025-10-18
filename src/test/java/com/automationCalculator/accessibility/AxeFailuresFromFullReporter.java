// src/test/java/com/automationCalculator/accessibility/AxeFailuresFromFullReporter.java
package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AxeFailuresFromFullReporter {

  static class FailItem {
    final String scenario;
    final List<Rule> rules;
    FailItem(String scenario, List<Rule> rules){ this.scenario=scenario; this.rules=rules; }
  }

  public static void main(String[] args) throws Exception {
    Path dir = args.length > 0 ? Path.of(args[0]) : Path.of("target","axe");
    Files.createDirectories(dir);

    // collect *.json (recursively)
    List<Path> jsons = new ArrayList<>();
    try (var walk = Files.walk(dir)) {
      walk.filter(p -> p.toString().endsWith(".json"))
          .filter(p -> !p.getFileName().toString().startsWith("FAIL-"))
          .forEach(jsons::add);
    }
    jsons.sort(Comparator.comparing(Path::toString));

    Set<String> skipRules = readCsv(System.getenv("AXE_SKIP_RULES"));
    Set<String> failImpacts = impactsAtOrAbove(System.getenv("AXE_MIN_IMPACT"));
    ObjectMapper mapper = new ObjectMapper();

    // group by feature (using file name prefix "Feature__Scenario")
    Map<String, List<FailItem>> byFeature = new LinkedHashMap<>();

    for (Path f : jsons) {
      Results r;
      try { r = mapper.readValue(Files.readAllBytes(f), Results.class); }
      catch (Exception e) { continue; }

      List<Rule> actionable = Optional.ofNullable(r.getViolations()).orElse(List.of())
          .stream()
          .filter(v -> !skipRules.contains(v.getId()))
          .filter(v -> v.getImpact() == null || failImpacts.contains(v.getImpact()))
          .collect(Collectors.toList());
      if (actionable.isEmpty()) continue;

      String base = baseName(f.getFileName().toString());          // Feature__Scenario
      String[] parts = base.split("__", 2);
      String feature = parts.length > 1 ? parts[0] : "Feature";
      String scenario = parts.length > 1 ? parts[1] : base;

      byFeature.computeIfAbsent(feature, k -> new ArrayList<>())
               .add(new FailItem(scenario, actionable));
    }

    StringBuilder body = new StringBuilder("<h1>Accessibility Failures</h1>");

    if (byFeature.isEmpty()) {
      body.append("<p><em>No scenarios with violations.</em></p>");
    } else {
      for (var entry : byFeature.entrySet()) {
        body.append("<h2>Feature: ").append(esc(entry.getKey())).append("</h2>");
        for (FailItem item : entry.getValue()) {
          body.append("<h3>").append(esc(item.scenario)).append("</h3>");
          for (Rule v : item.rules) {
            String impact = v.getImpact() == null ? "unknown" : v.getImpact();
            body.append("<h4>").append(esc(v.getId())).append(" (").append(esc(impact)).append(")</h4>");
            body.append("<p>").append(esc(v.getDescription())).append("</p>");
            if (v.getHelpUrl() != null && !v.getHelpUrl().isBlank()) {
              body.append("<p><a href=\"").append(esc(v.getHelpUrl())).append("\">")
                  .append(esc(v.getHelpUrl())).append("</a></p>");
            }
          }
        }
      }
    }

    String html = """
      <!doctype html><html><head><meta charset="utf-8"><title>Accessibility Failures</title>
      <style>
        body{font-family:system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;line-height:1.4}
        h1{margin-top:0} h2{margin-top:28px} h3{margin:18px 0 6px} h4{margin:12px 0 4px}
        p{margin:0 0 8px}
      </style></head><body>""" + body + "</body></html>";

    Files.writeString(dir.resolve("failures.html"), html, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    System.out.println("[axe] Wrote grouped fail-only report: " + dir.resolve("failures.html").toAbsolutePath());
  }

  private static String baseName(String name){ return name.replaceFirst("\\.json$","").replaceFirst("-\\d+$",""); }
  private static String esc(String s){ return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
  private static Set<String> readCsv(String raw){
    if (raw == null || raw.isBlank()) return Set.of();
    return Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
  }
  private static Set<String> impactsAtOrAbove(String min){
    String m=(min==null?"minor":min.trim().toLowerCase());
    return switch(m){
      case "critical" -> Set.of("critical");
      case "serious"  -> Set.of("serious","critical");
      case "moderate" -> Set.of("moderate","serious","critical");
      default         -> Set.of("minor","moderate","serious","critical");
    };
  }
}
