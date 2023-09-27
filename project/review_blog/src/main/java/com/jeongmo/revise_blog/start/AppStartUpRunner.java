package com.jeongmo.revise_blog.start;

import com.jeongmo.revise_blog.domain.Article;
import com.jeongmo.revise_blog.dto.user.AddUserRequest;
import com.jeongmo.revise_blog.repository.ArticleRepository;
import com.jeongmo.revise_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartUpRunner implements CommandLineRunner {

    @Autowired
    UserService userService; // Assuming you have a UserService

    @Autowired
    ArticleRepository articleRepository;


    @Override
    public void run(String... args) throws Exception {

        // Define the user details
        saveUser("Author1@gmail.com", "author1pw", "Author1");
        saveUser("Author2@gmail.com", "author2pw", "Author2");

        articleRepository.save(Article.builder()
                .title("The sun-kissed beach")
                .content("The sun-kissed beach was a veritable haven. Soft, white sand stretched lazily from one end of the coastline to the other, inviting visitors to take off their shoes and dip their toes in the cool water.")
                .author(userService.getUserByEmail("Author1@gmail.com"))
                .build());
        articleRepository.save(Article.builder()
                .title("The older man")
                .content("The older man shuffled slowly down the dirt road, a faded baseball cap pulled low over his eyes to shield them from the sun’s rays. He wore overalls and a flannel shirt, his hands calloused from years of hard work in the fields.")
                .author(userService.getUserByEmail("Author2@gmail.com"))
                .build());
    }

    private void saveUser(String email, String password, String nickname) {
        if (!userService.isDuplicatedEmail(email)) {
            // Create a new user and save it to the database
            userService.save(new AddUserRequest(email, password, nickname));
        }
    }
}
