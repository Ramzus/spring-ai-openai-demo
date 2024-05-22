package com.adeo.summit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ResourceProperties {

    @Value("classpath:*.pdf")
    private Resource[] resources;

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

}
