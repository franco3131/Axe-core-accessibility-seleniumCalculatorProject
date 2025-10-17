package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AxeFailuresFromFullReporter {

  public static void main(String[] args) throws Exception {
    Path dir = args.length > 0 ? Path.of(args[0]) : Path.of("target","axe");
    Files.createDirectories(dir);

    // Gather *.json (recursively, just in case)
    List<Path> jsons = new ArrayList<>();
    try (var walk = Files.walk(dir)) {
      walk.filter(p -> p.toString().endsWith(".json"))
          .filter(p -> !p.getFileName().toString().startsWith("FAIL-")) // we only need full results
          .forEach(jsons::add);
    }
    jsons.sort(Comparator.comparing(Path::toString));

    // Read env filters (same semantics as AxeChecks)
    Set<String> skipRules = readCsv(System.getenv("AXE_SKIP_RULES"));
    Set<String> failImpacts = impactsAtOrAbove(System.getenv("AXE_MIN_IMPACT"));

    ObjectMapper mapper = new ObjectMapper();
    StringBuilder body = new StringBuilder();
    body.append("<h1>Accessibility Failures</h1>");

    int failed = 0;
    for (Path f : jsons) {
      Results r;
      try {
        r = mapper.readValue(Files.readAllBytes(f), Results.class);
      } catch (Exception e) {
        continue;
      }
      List<Rule> violations = Optional.ofNullable(r.getViolations()).orElse(List.of());
      List<Rule> actionable = violations.stream()
          .filter(v -> !skipRules.contains(v.getId()))
          .filter(v -> v.getImpact() == null || failImpacts.contains(v.getImpact()))
          .collect(Collectors.toList());
      if (actionable.isEmpty()) continue;

      failed++;
      String scenario = base(f.getFileName().toString());
      body.append("<h2>").append(esc(scenario)).append("</h2>");
      for (Rule v : actionable) {
        String impact = v.getImpact() == null ? "unknown" : v.getImpact();
        body.append("<h3>").append(esc(v.getId())).append(" (").append(esc(impact)).append(")</h3>");
        body.append("<p>").append(esc(v.getDescription())).append("</p>");
        if (v.getHelpUrl() != null && !v.getHelpUrl().isBlank()) {
          body.append("<p><a href=\"").append(esc(v.getHelpUrl())).append("\">")
              .append(esc(v.getHelpUrl())).append("</a></p>");
        }
      }
    }

    if (failed == 0) {
      body.append("<p><em>No scenarios with violations.</em></p>");
    }

    String html = """
      <!doctype html><html><head><meta charset="utf-8"><title>Accessibility Failures</title>
      <style>body{font-family:system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;line-height:1.4}
      h1{margin-top:0} h2{margin-top:24px} h3{margin:12px 0 4px} p{margin:0 0 8px}</style>
      </head><body>""" + body + "</body></html>";

    Path out = dir.resolve("failures.html");
    Files.writeString(out, html, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    System.out.println("[axe] Wrote fail-only report from full JSONs: " + out.toAbsolutePath());
  }

  private static String base(String name) {
    return name.replaceFirst("\\.json$","").replaceFirst("-\\d+$","");
  }
  private static String esc(String s) {
    if (s == null) return "";
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
  }
  private static Set<String> readCsv(String raw) {
    if (raw == null || raw.isBlank()) return Set.of();
    return Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
  }
  private static Set<String> impactsAtOrAbove(String min) {
    String m = (min == null ? "minor" : min.trim().toLowerCase());
    return switch (m) {
      case "critical" -> Set.of("critical");
      case "serious"  -> Set.of("serious","critical");
      case "moderate" -> Set.of("moderate","serious","critical");
      default         -> Set.of("minor","moderate","serious","critical");
    };
  }
}
