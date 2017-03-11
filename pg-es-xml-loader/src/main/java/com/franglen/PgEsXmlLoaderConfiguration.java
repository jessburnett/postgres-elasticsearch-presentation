package com.franglen;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgEsXmlLoaderConfiguration {

    @Bean
    public Mapper mapper() {
        return new DozerBeanMapper();
    }

}
