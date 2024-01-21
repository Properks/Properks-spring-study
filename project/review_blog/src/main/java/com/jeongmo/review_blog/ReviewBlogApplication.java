package com.jeongmo.review_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReviewBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewBlogApplication.class, args);
	}

}
