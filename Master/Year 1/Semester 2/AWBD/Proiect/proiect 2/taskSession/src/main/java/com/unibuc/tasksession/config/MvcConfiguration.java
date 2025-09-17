package com.unibuc.tasksession.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        r.setDefaultErrorView("defaultException");
        r.setExceptionAttribute("ex");

        Properties mappings = new Properties();
        mappings.setProperty("NumberFormatException", "numberFormatException");
        r.setExceptionMappings(mappings);

        Properties statusCodes = new Properties();
        statusCodes.setProperty("NumberFormatException", "400");
        r.setStatusCodes(statusCodes);

        return r;
    }
}
