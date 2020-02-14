package com.wzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StartUp {

  public static void main(String[] args) {
    SpringApplication.run(StartUp.class, args);
  }
}
