package com.example.lab3.library.config;

import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Incarca si compileaza fisierul cu reguli doar pentru politica Drools.
@Configuration
@ConditionalOnProperty(name = "library.policy", havingValue = "drools")
public class DroolsConfig {

    @Bean
    public KieContainer kieContainer(LibraryProperties properties, ResourceLoader resourceLoader) throws IOException {
        // Regulile raman intr-un fisier separat de logica Java.
        String rules = resourceLoader.getResource(properties.getRulesFile())
                .getContentAsString(StandardCharsets.UTF_8);

        KieServices kieServices = KieServices.Factory.get();
        var fileSystem = kieServices.newKieFileSystem();
        fileSystem.write("src/main/resources/rules/borrowing-rules.drl", rules);

        var results = kieServices.newKieBuilder(fileSystem).buildAll().getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("Could not compile borrowing rules: " + results.getMessages());
        }

        ReleaseId releaseId = kieServices.getRepository().getDefaultReleaseId();
        return kieServices.newKieContainer(releaseId);
    }
}
