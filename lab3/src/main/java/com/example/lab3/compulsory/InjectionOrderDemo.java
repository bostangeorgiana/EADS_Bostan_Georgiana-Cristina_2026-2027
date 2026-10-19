package com.example.lab3.compulsory;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Demonstreaza toate punctele de injectie (slide 7) si ORDINEA lor:
// constructor -> field -> setter & method -> @PostConstruct (slide 21)
@Component
public class InjectionOrderDemo {

    private int step = 0;

    // 1. CONSTRUCTOR injection (recomandata, pentru dependente obligatorii)
    private final ConstructorDependency constructorDep;

    // 2. FIELD injection (cea mai scurta, nerecomandata)
    @Autowired
    private FieldDependency fieldDep;

    // 3. SETTER injection (pentru dependente optionale)
    private SetterDependency setterDep;

    // 4. METHOD injection (orice metoda cu @Autowired, mai rar folosita)
    private MethodDependency methodDep;

    public InjectionOrderDemo(ConstructorDependency constructorDep) {
        this.constructorDep = constructorDep;
        System.out.println("\n========== COMPULSORY: INJECTION ORDER ==========");
        log("CONSTRUCTOR");   // aici fieldDep, setterDep, methodDep sunt inca null
    }

    @Autowired
    public void setSetterDep(SetterDependency setterDep) {
        this.setterDep = setterDep;
        log("SETTER");        // aici fieldDep e deja injectat
    }

    @Autowired
    public void prepare(MethodDependency methodDep) {   // numele metodei poate fi oricare
        this.methodDep = methodDep;
        log("METHOD");
    }

    @PostConstruct
    public void afterInjection() {
        log("@PostConstruct"); // dupa ce TOATE injectiile s-au terminat
        System.out.println("=================================================\n");
    }

    // Afiseaza pasul curent si ce dependente sunt deja injectate in acel moment
    private void log(String point) {
        step++;
        System.out.printf("[%d] %-15s constructor=%-4s field=%-4s setter=%-4s method=%-4s%n",
                step, point, st(constructorDep), st(fieldDep), st(setterDep), st(methodDep));
    }

    private static String st(Object dependency) {
        return dependency == null ? "null" : "OK";
    }
}
