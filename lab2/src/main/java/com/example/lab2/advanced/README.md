# Lab 2 - Advanced: Reflection vs ASM la component scanning

Toate comenzile se ruleaza din folderul `lab2`.

```
java src/main/java/com/example/lab2/advanced/Generator.java 1000
./mvnw -q compile
CP="target/classes:$HOME/.m2/repository/org/springframework/spring-core/7.0.9/spring-core-7.0.9.jar"

java -cp "$CP" com.example.lab2.advanced.Experiment verify      # c) + d)
java -cp "$CP" com.example.lab2.advanced.Experiment time        # e)

# f) clase incarcate din pachetul scanat / total
java -Xlog:class+load -cp "$CP" com.example.lab2.advanced.Experiment asm        | grep -c "com.example.lab2.advanced.components"
java -Xlog:class+load -cp "$CP" com.example.lab2.advanced.Experiment reflection | grep -c "com.example.lab2.advanced.components"
java -Xlog:class+load -cp "$CP" com.example.lab2.advanced.Experiment asm        | grep -c "class,load"
java -Xlog:class+load -cp "$CP" com.example.lab2.advanced.Experiment reflection | grep -c "class,load"

# g) dependenta lipsa
rm target/classes/com/example/lab2/advanced/external/ExternalLib.class
java -cp "$CP" com.example.lab2.advanced.Experiment verify

./mvnw clean package -DskipTests   # refacere la final
```

## Rezultate

| Criteriu                         | Reflection (Class.forName)          | ASM (bytecode)      |
|----------------------------------|-------------------------------------|---------------------|
| Componente gasite                | 101                                 | 101                 |
| Timp mediu (5 warm-up + 10 rulari)| ___ ms                             | ___ ms              |
| Clase incarcate din pachet       | 1001                                | 0                   |
| Total clase incarcate de JVM     | ___                                 | ___                 |
| Blocuri static rulate            | 40 (si in clase fara adnotare)      | 0                   |
| Dependenta lipsa                 | NoClassDefFoundError, gaseste 100   | gaseste toate 101   |

Concluzie: ASM da acelasi rezultat, dar e mai rapid, nu incarca clase, nu ruleaza
blocuri static si nu cade cand lipseste o dependenta (slide 22).
