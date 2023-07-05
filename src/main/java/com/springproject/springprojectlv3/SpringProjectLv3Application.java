package com.springproject.springprojectlv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringProjectLv3Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectLv3Application.class, args);
    }

}
