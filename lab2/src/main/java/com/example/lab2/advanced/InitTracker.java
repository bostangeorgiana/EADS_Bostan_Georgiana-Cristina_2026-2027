package com.example.lab2.advanced;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class InitTracker {
    private static final Set<String> INITIALIZED = ConcurrentHashMap.newKeySet();

    public static void mark(String className) { INITIALIZED.add(className); }
    public static int count() { return INITIALIZED.size(); }
}
