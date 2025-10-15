package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AxeFailOnlyReporter {
  public static void main(String[] args) throws Exception {
    Path dir = args.length > 0 ? Path.of(args[0]) : Path.of("target","axe");
    Files.createDirectories(dir);

    List<Path> jsons;
    try (var s = Files.list(dir)) {
      jsons = s.filter(p -> p.toString().endsWith(".json")).sorted().collect(Collectors.toList());
    }

    ObjectMapper mapper = new ObjectMapper();
    StringBuilder body = new StringBuilder();
    body.append("<h1>Accessibility Failures</h1>");

    int totalFailedScenarios = 0;

    for (Path f : jsons) {
      Results r;
      try { r = mapper.readValue(Files.readAllBytes(f), Results.class); }
      catch (Exception e) { continue; }

      List<Rule> violations = r.getViolations();
      if (violations == null || violations.isEmpty()) continue;  // skip clean scenarios

      String scenario = baseName(f.getFileName().toString());
      totalFailedScenarios++;

      body.append("<h2>").append(esc(scenario)).append("</h2>");
      for (Rule rule : violations) {
        String impact = rule.getImpact() == null ? "unknown" : rule.getImpact();
        body.append("<h3>").append(esc(rule.getId())).append(" (").append(esc(impact)).append(")</h3>");
        body.append("<p>").append(esc(rule.getDescription())).append("</p>");
        if (rule.getHelpUrl() != null) {
          body.append("<p><a href=\"").append(esc(rule.getHelpUrl())).append("\">")
              .append(esc(rule.getHelpUrl())).append("</a></p>");
        }
      }
    }

    if (totalFailedScenarios == 0) {
      body.append("<p><em>No scenarios with violations.</em></p>");
    }

    String html = """
      <!doctype html><html><head><meta charset="utf-8"><title>Accessibility Failures</title>
      <style>body{font-family:system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;line-height:1.4}
      h1{margin-top:0} h2{margin-top:24px} h3{margin:12px 0 4px} p{margin:0 0 8px}</style>
      </head><body>""" + body + "</body></html>";

    Path out = dir.resolve("failures.html");
    Files.writeString(out, html, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    System.out.println("[axe] Wrote fail-only report: " + out.toAbsolutePath());
  }

  private static String baseName(String name) {
    String b = name.replaceFirst("\\.json$","");
    return b.replaceFirst("-\\d+$",""); // strip -N suffix if present
  }
  private static String esc(String s) {
    if (s == null) return "";
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
  }
}
