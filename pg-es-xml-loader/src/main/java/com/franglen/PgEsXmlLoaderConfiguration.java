package com.franglen;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/integration.xml")
public class PgEsXmlLoaderConfiguration {

    @Bean
    public Mapper mapper() {
        return new DozerBeanMapper();
    }

}
