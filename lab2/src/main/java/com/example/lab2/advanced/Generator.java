package com.example.lab2.advanced;

import java.nio.file.Files;
import java.nio.file.Path;

// java src/main/java/com/example/lab2/advanced/Generator.java 1000
public class Generator {
    public static void main(String[] args) throws Exception {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
        Path dir = Path.of("src/main/java/com/example/lab2/advanced/components/gen");
        Files.createDirectories(dir);

        for (int i = 0; i < n; i++) {
            String name = String.format("Class%04d", i);
            boolean annotated = i % 10 == 0;  // adnotare @MyComponent
            boolean hasStatic = i % 25 == 0;  // bloc static

            StringBuilder sb = new StringBuilder();
            sb.append("package com.example.lab2.advanced.components.gen;\n\n");
            sb.append("import com.example.lab2.advanced.InitTracker;\n");
            sb.append("import com.example.lab2.advanced.MyComponent;\n\n");
            if (annotated) sb.append("@MyComponent\n");
            sb.append("public class ").append(name).append(" {\n");
            if (hasStatic) {
                sb.append("    static { InitTracker.mark(\"").append(name).append("\"); }\n");
            }
            sb.append("    public int value() { return ").append(i).append("; }\n");
            sb.append("}\n");

            Files.writeString(dir.resolve(name + ".java"), sb.toString());
        }
        System.out.println("Generated " + n + " classes in " + dir);
    }
}
