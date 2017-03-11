package com.franglen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.franglen.service.DocumentReader;

@SpringBootApplication
public class PgEsXmlLoaderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PgEsXmlLoaderApplication.class, args);
    }

    private final DocumentReader reader;

    public PgEsXmlLoaderApplication(DocumentReader reader) {
        this.reader = reader;
    }

    public void run(String... args) throws Exception {
        reader.read(args[0]);
    }

}
