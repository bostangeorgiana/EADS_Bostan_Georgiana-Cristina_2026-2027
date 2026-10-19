package com.example.lab2.advanced;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public final class ClassFiles {

    public static List<Path> list(Path root) throws IOException {
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(p -> p.toString().endsWith(".class")).sorted().toList();
        }
    }

    public static String toClassName(Path classesDir, Path file) {
        String rel = classesDir.relativize(file).toString();
        return rel.substring(0, rel.length() - ".class".length())
                  .replace(File.separatorChar, '.');
    }
}
