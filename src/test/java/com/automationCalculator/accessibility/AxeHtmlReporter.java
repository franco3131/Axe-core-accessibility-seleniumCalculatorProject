package com.automationCalculator.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AxeHtmlReporter {

    public static void main(String[] args) throws Exception {
        Path dir = args.length > 0 ? Path.of(args[0]) : Path.of("target", "axe");
        Files.createDirectories(dir);

        List<Path> files;
        try (var s = Files.list(dir)) {
            files = s.filter(p -> p.toString().endsWith(".json"))
                     .sorted()
                     .collect(Collectors.toList());
        }

        ObjectMapper mapper = new ObjectMapper();
        int totalViolations = 0;
        StringBuilder body = new StringBuilder();
        body.append("<h1>Axe Accessibility Report</h1>");
        body.append("<p>Generated: ").append(LocalDateTime.now()).append("</p>");

        if (files.isEmpty()) {
            body.append("<p><em>No axe JSON files found in ")
                .append(escape(dir.toAbsolutePath().toString()))
                .append("</em></p>");
        } else {
            body.append("<table><thead><tr><th>Page</th><th>Violating Rules</th></tr></thead><tbody>");
        }

        Map<String,Integer> impactCounts = new LinkedHashMap<>();
        List<String> detailsSections = new ArrayList<>();

        for (Path f : files) {
            Results r;
            try {
                r = mapper.readValue(Files.readAllBytes(f), Results.class);
            } catch (IOException e) {
                body.append("<p style='color:#b00'>Failed to read ")
                    .append(escape(f.getFileName().toString()))
                    .append(": ").append(escape(e.getMessage())).append("</p>");
                continue;
            }
            List<Rule> violations = r.getViolations();
            int count = (violations == null ? 0 : violations.size());
            totalViolations += count;

            String page = f.getFileName().toString().replace(".json","");
            body.append("<tr><td>").append(escape(page)).append("</td><td>")
                .append(count).append("</td></tr>");

            if (violations != null) {
                for (Rule rule : violations) {
                    String impact = rule.getImpact() == null ? "unknown" : rule.getImpact();
                    impactCounts.merge(impact, 1, Integer::sum);

                    StringBuilder sec = new StringBuilder();
                    sec.append("<h3>").append(escape(page)).append(" — ")
                       .append(escape(rule.getId())).append(" (")
                       .append(escape(impact)).append(")</h3>");
                    sec.append("<p>").append(escape(rule.getDescription())).append("</p>");
                    if (rule.getHelpUrl() != null) {
                        sec.append("<p><a href=\"").append(escape(rule.getHelpUrl()))
                           .append("\">").append(escape(rule.getHelpUrl())).append("</a></p>");
                    }
                    if (rule.getNodes() != null && !rule.getNodes().isEmpty()) {
                        sec.append("<details><summary>Affected elements (")
                           .append(rule.getNodes().size()).append(")</summary><ul>");
                        rule.getNodes().forEach(n -> {
                            String target = n.getTarget() == null ? "" : n.getTarget().toString();
                            String html = n.getHtml() == null ? "" : n.getHtml();
                            sec.append("<li><code>").append(escape(target)).append("</code><br>")
                               .append("<pre>").append(escape(snippet(html, 500))).append("</pre></li>");
                        });
                        sec.append("</ul></details>");
                    }
                    detailsSections.add(sec.toString());
                }
            }
        }

        if (!files.isEmpty()) {
            body.append("</tbody></table>");
            body.append("<h2>Violations by Impact</h2><ul>");
            if (impactCounts.isEmpty()) {
                body.append("<li>None</li>");
            } else {
                for (var e : impactCounts.entrySet()) {
                    body.append("<li>").append(escape(e.getKey()))
                        .append(": ").append(e.getValue()).append("</li>");
                }
            }
            body.append("</ul>");
            body.append("<h2>Details</h2>");
            detailsSections.forEach(body::append);
        }

        String html = """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8">
              <title>Axe Accessibility Report</title>
              <style>
                body{font-family:system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:24px;line-height:1.4}
                table{border-collapse:collapse;width:100%;margin:16px 0}
                th,td{border:1px solid #ddd;padding:8px}
                th{background:#f5f5f5;text-align:left}
                code{background:#f0f0f0;padding:2px 4px;border-radius:4px}
                pre{background:#f8f8f8;padding:8px;border-radius:6px;overflow:auto;max-height:220px}
                details{margin:8px 0}
              </style>
            </head>
            <body>%s</body>
            </html>
            """.formatted(body.toString());

        Path index = dir.resolve("index.html");
        Files.writeString(index, html, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("[axe] Wrote HTML report: " + index.toAbsolutePath());
        System.out.println("[axe] Total violating rules: " + totalViolations);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
    private static String snippet(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "…" : s;
    }
}
