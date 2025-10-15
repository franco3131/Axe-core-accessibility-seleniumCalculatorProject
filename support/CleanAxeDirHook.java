// src/test/java/com/automationCalculator/support/CleanAxeDirHook.java
package com.automationCalculator.support;

import io.cucumber.java.Before;

import java.nio.file.*;

public class CleanAxeDirHook {
    private static volatile boolean cleaned = false;

    @Before(order = 0)
    public void cleanOnce() {
        if (cleaned) return;
        cleaned = true;
        try {
            Path dir = Path.of("target","axe");
            if (Files.isDirectory(dir)) {
                Files.walk(dir)
                     .sorted((a,b) -> b.getNameCount()-a.getNameCount())
                     .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
            }
            Files.createDirectories(dir);
            System.out.println("[axe] cleaned target/axe");
        } catch (Exception e) {
            System.err.println("[axe] failed to clean target/axe: " + e.getMessage());
        }
    }
}
