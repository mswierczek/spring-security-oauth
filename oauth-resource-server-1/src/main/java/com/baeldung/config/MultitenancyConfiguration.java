package com.baeldung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"com.baeldung.multitenancy"})
public class MultitenancyConfiguration {

    @Bean
    public List<String> allowedDomains() {
        return Collections.singletonList("baeldung.com");
    }


}
