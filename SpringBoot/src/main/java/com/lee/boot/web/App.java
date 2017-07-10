package com.lee.boot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.easywit.converter", "com.sea.web"})
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
