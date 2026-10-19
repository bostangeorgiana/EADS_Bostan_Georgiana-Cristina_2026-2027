package com.example.lab2.advanced;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.SpringAsmInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

public class AsmScanner {

    private static final String DESC = "Lcom/example/lab2/advanced/MyComponent;";

    public static Set<String> scan(Path classesDir, String pkg) throws IOException {
        Path root = classesDir.resolve(pkg.replace('.', '/'));
        Set<String> found = new TreeSet<>();

        for (Path file : ClassFiles.list(root)) {

            ClassReader reader = new ClassReader(Files.readAllBytes(file));
            boolean[] has = {false};

            reader.accept(new ClassVisitor(SpringAsmInfo.ASM_VERSION) {
                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                    if (DESC.equals(descriptor)) has[0] = true;
                    return null;
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            if (has[0]) {
                found.add(reader.getClassName().replace('/', '.'));
            }
        }
        return found;
    }
}
