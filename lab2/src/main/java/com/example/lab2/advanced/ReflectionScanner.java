package com.example.lab2.advanced;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// adnotarea
public class ReflectionScanner {

    public record Result(Set<String> components, List<String> errors) {}

    @SuppressWarnings("unchecked")
    public static Result scan(Path classesDir, String pkg, ClassLoader loader) throws Exception {
        Path root = classesDir.resolve(pkg.replace('.', '/'));

        Class<? extends Annotation> ann = (Class<? extends Annotation>)
                Class.forName("com.example.lab2.advanced.MyComponent", true, loader);

        Set<String> found = new TreeSet<>();
        List<String> errors = new ArrayList<>();

        for (Path file : ClassFiles.list(root)) {
            String name = ClassFiles.toClassName(classesDir, file);
            try {
                Class<?> c = Class.forName(name, true, loader);
                if (c.isAnnotationPresent(ann)) {
                    found.add(name);
                }
            } catch (Throwable t) {
                errors.add(name + " -> " + t);
            }
        }
        return new Result(found, errors);
    }
}
