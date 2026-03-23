package com.car.management.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
