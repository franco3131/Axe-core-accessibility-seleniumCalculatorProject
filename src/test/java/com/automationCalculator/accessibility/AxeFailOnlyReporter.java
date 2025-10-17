package com.automationCalculator.accessibility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AxeFailOnlyReporter {

  record FailRule(String id, String impact, String description, String helpUrl) {}

  public static void main(String[] args) throws Exception {
    Path dir = args.length > 0 ? Path.of(args[0]) : Path.of("target","axe");
    Files.createDirectories(dir);

    List<Path> jsons;
    try (var s = Files.list(dir)) {
      jsons = s.filter(p -> p.getFileName().toString().startsWith("FAIL-"))
               .filter(p -> p.toString().endsWith(".json"))
               .sorted()
               .collect(Collectors.toList());
    }

    ObjectMapper mapper = new ObjectMapper();
    StringBuilder body = new StringBuilder();
    body.append("<h1>Accessibility Failures</h1>");

    if (jsons.isEmpty()) {
      body.append("<p><em>No scenarios with violations.</em></p>");
    } else {
      for (Path f : jsons) {
        String scenario = f.getFileName().toString().replaceFirst("^FAIL-","").replaceFirst("\\.json$","");
        List<FailRule> rules;
        try {
          rules = mapper.readValue(Files.readAllBytes(f), new TypeReference<List<FailRule>>(){});
        } catch (Exception e) {
          continue;
        }
        if (rules.isEmpty()) continue;

        body.append("<h2>").append(esc(scenario)).append("</h2>");
        for (FailRule r : rules) {
          body.append("<h3>").append(esc(r.id())).append(" (").append(esc(nullToEmpty(r.impact()))).append(")</h3>");
          body.append("<p>").append(esc(nullToEmpty(r.description()))).append("</p>");
          if (r.helpUrl() != null && !r.helpUrl().isBlank()) {
            body.append("<p><a href=\"").append(esc(r.helpUrl())).append("\">")
                .append(esc(r.helpUrl())).append("</a></p>");
          }
        }
      }
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

  private static String esc(String s){ return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
  private static String nullToEmpty(String s){ return s==null?"":s; }
}
