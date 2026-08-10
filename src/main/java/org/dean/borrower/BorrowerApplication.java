package org.dean.borrower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BorrowerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowerApplication.class, args);
    }

    public String HelloWorld() {
        return "hello world";
    }
}
