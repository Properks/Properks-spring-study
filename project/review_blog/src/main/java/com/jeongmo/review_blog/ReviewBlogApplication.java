package com.jeongmo.review_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@EnableSpringConfigured
@SpringBootApplication
public class ReviewBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewBlogApplication.class, args);
	}

}
