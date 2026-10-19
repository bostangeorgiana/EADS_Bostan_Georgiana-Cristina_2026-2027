package com.example.lab2.advanced;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

//   java -cp "$CP" com.example.lab2.advanced.Experiment verify|time|asm|reflection
public class Experiment {

    static final Path CLASSES = Path.of("target/classes");
    static final String PKG = "com.example.lab2.advanced.components";

    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0] : "verify";
        switch (mode) {
            case "verify" -> verify();
            case "time" -> time();
            case "asm" -> System.out.println("ASM found: " + AsmScanner.scan(CLASSES, PKG).size());
            case "reflection" -> {
                var r = ReflectionScanner.scan(CLASSES, PKG, Experiment.class.getClassLoader());
                System.out.println("Reflection found: " + r.components().size()
                        + ", errors: " + r.errors().size());
            }
            default -> System.out.println("Moduri: verify | time | asm | reflection");
        }
    }

    // rezultate + initializari + erori la dependenta lipsa
    static void verify() throws Exception {
        System.out.println("Initialized at start:         " + InitTracker.count());

        // ASM
        Set<String> asm = AsmScanner.scan(CLASSES, PKG);
        System.out.println("ASM found:                    " + asm.size());
        System.out.println("Initialized after ASM:        " + InitTracker.count());

        // Reflection
        var refl = ReflectionScanner.scan(CLASSES, PKG, Experiment.class.getClassLoader());
        System.out.println("Reflection found:             " + refl.components().size());
        System.out.println("Initialized after Reflection: " + InitTracker.count());
        System.out.println("Reflection errors:            " + refl.errors());

        // comparam
        Set<String> onlyAsm = new TreeSet<>(asm);
        onlyAsm.removeAll(refl.components());
        Set<String> onlyRefl = new TreeSet<>(refl.components());
        onlyRefl.removeAll(asm);
        System.out.println("Same result?                  " + (onlyAsm.isEmpty() && onlyRefl.isEmpty()));
        System.out.println("Only ASM found:               " + onlyAsm);
        System.out.println("Only Reflection found:        " + onlyRefl);
    }

    // e) 5 rulari de incalzire (warm-up), apoi 10 rulari masurate
    static void time() throws Exception {
        int warmup = 5, runs = 10;
        URL url = CLASSES.toUri().toURL();

        for (int i = 0; i < warmup; i++) {   // incalzim JVM-ul (JIT), nu masuram
            AsmScanner.scan(CLASSES, PKG);
            reflectionFresh(url);
        }

        double[] asmMs = new double[runs], reflMs = new double[runs];
        for (int i = 0; i < runs; i++) {
            long t0 = System.nanoTime();
            AsmScanner.scan(CLASSES, PKG);
            asmMs[i] = (System.nanoTime() - t0) / 1_000_000.0;

            t0 = System.nanoTime();
            reflectionFresh(url);
            reflMs[i] = (System.nanoTime() - t0) / 1_000_000.0;
        }
        System.out.printf("ASM        avg = %.2f ms, min = %.2f ms%n",
                Arrays.stream(asmMs).average().orElse(0), Arrays.stream(asmMs).min().orElse(0));
        System.out.printf("Reflection avg = %.2f ms, min = %.2f ms%n",
                Arrays.stream(reflMs).average().orElse(0), Arrays.stream(reflMs).min().orElse(0));
    }

    // ClassLoader NOU la fiecare rulare: altfel, dupa prima rulare clasele raman
    // deja incarcate si Reflection ar parea artificial de rapid
    static void reflectionFresh(URL url) throws Exception {
        try (URLClassLoader loader = new URLClassLoader(new URL[]{url}, ClassLoader.getPlatformClassLoader())) {
            ReflectionScanner.scan(CLASSES, PKG, loader);
        }
    }
}
