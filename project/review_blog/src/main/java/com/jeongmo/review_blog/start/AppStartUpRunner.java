package com.jeongmo.review_blog.start;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.dto.user.AddUserRequest;
import com.jeongmo.review_blog.repository.ArticleRepository;
import com.jeongmo.review_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartUpRunner implements CommandLineRunner {
    // CommandLineRunner is interface for running some code when SpringBoot is executed.
    // If i implement CommandLineRunner and add @Component annotation, run method is executed after Springboot scan component

    @Autowired
    UserService userService; // Assuming you have a UserService

    @Autowired
    ArticleRepository articleRepository;


    @Override
    public void run(String... args) throws Exception {

        // Define the user details
        saveUser("Author1@gmail.com", "author1pw", "Author1");
        saveUser("Author2@gmail.com", "author2pw", "Author2");

        saveArticle("The sun-kissed beach",
                "The sun-kissed beach was a veritable haven. Soft, white sand stretched " +
                        "lazily from one end of the coastline to the other, inviting visitors to take off their shoes and dip" +
                        " their toes in the cool water.",
                "Author1@gmail.com");

        saveArticle("The older man",
                "The older man shuffled slowly down the dirt road, a faded baseball cap pulled low over his eyes to " +
                        "shield them from the sun’s rays. He wore overalls and a flannel shirt, his hands calloused " +
                        "from years of hard work in the fields.",
                "Author2@gmail.com");
    }

    /**
     * Save user with email, password and nickname
     *
     * @param email The email of user
     * @param password The password of user
     * @param nickname The nickname of user
     */
    private void saveUser(String email, String password, String nickname) {
        if (!userService.isDuplicatedEmail(email)) {
            // Create a new user and save it to the database
            userService.save(new AddUserRequest(email, password, nickname));
        }
    }

    /**
     * Save article with title, content and email of user
     *
     * @param title The title of article
     * @param content The content of article
     * @param email The email of writer
     */
    private void saveArticle(String title, String content, String email) {
        if (userService.existsByEmail(email)) {
            articleRepository.save(Article.builder()
                    .title(title)
                    .content(content)
                    .author(userService.getUserByEmail(email))
                    .build());
        }
    }
}
