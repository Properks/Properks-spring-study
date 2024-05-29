//package com.jeongmo.review_blog.start;
//
//import com.jeongmo.review_blog.domain.Article;
//import com.jeongmo.review_blog.domain.Category;
//import com.jeongmo.review_blog.dto.category.CreateCategoryRequest;
//import com.jeongmo.review_blog.dto.user.AddUserRequest;
//import com.jeongmo.review_blog.repository.ArticleRepository;
//import com.jeongmo.review_blog.repository.CategoryRepository;
//import com.jeongmo.review_blog.service.CategoryService;
//import com.jeongmo.review_blog.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class AppStartUpRunner implements CommandLineRunner {
//    // CommandLineRunner is interface for running some code when SpringBoot is executed.
//    // If i implement CommandLineRunner and add @Component annotation, run method is executed after Springboot scan component
//
//    @Autowired
//    UserService userService; // Assuming you have a UserService
//
//    @Autowired
//    ArticleRepository articleRepository;
//
//    @Autowired
//    CategoryService categoryService;
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // Define the user details
//        saveUser("Author1@gmail.com", "author1pw", "Author1");
//        saveUser("Author2@gmail.com", "author2pw", "Author2");
//        saveUser("Author3@gmail.com", "author3pw", "Author3");
//
//        final String category = "Category";
//        Category testCategory1 = saveCategory(null, category);
//        Category testCategory2 = saveCategory(category, "Category2");
//
//
//        System.out.println(categoryService.findCategory(category).getChildren());
//        /**
//         * Add articles
//         * Source: https://theteachingcouple.com/the-top-20-descriptive-paragraph-examples/
//         */
//        saveArticle("The sun-kissed beach",
//                "The sun-kissed beach was a veritable haven. Soft, white sand stretched " +
//                        "lazily from one end of the coastline to the other, inviting visitors to take off their shoes and dip" +
//                        " their toes in the cool water.",
//                "Author1@gmail.com", testCategory1);
//        saveArticle("The older man",
//                "The older man shuffled slowly down the dirt road, a faded baseball cap pulled low over his eyes to " +
//                        "shield them from the sun’s rays. He wore overalls and a flannel shirt, his hands calloused " +
//                        "from years of hard work in the fields.",
//                "Author2@gmail.com", testCategory2);
//        saveArticle("The bustling city",
//                "The bustling city street was a melting pot of cultures, languages, and flavours. Merchants called " +
//                        "out from their booths as eager shoppers stopped to examine their wares. Everywhere I looked, people were walking, talking, laughing- the occasional honk of a car horn punctuating the air.",
//                "Author3@gmail.com", testCategory1);
//        saveArticle("The lush meadow",
//                "The lush meadow was blanketed in soft green grass, punctuated with wildflowers in all rainbow " +
//                        "colours. The sun shone brightly in the sky, warming everything beneath it with its gentle rays. In the distance, a stream babbled peacefully as birds chirped their songs from the trees that lined it.",
//                "Author2@gmail.com", testCategory1);
//        saveArticle("The forest",
//                "The forest was alive with activity; small creatures skittered through the undergrowth while " +
//                        "bright-coloured birds flitted from branch to branch overhead. A cool breeze caressed my skin and rustled through the leaves of nearby trees as I walked along the path, breathing deeply of the damp woodland air.",
//                "Author3@gmail.com", testCategory2);
//        saveArticle("The ancient",
//                "The ancient ruins surrounded a vast desert, their sand-covered stones looking out over miles of " +
//                        "wind-swept dunes. I walked through the crumbling archway and into the courtyard, taking in the eerie silence that pervaded the entire site.",
//                "Author1@gmail.com", testCategory1);
//        saveArticle("The decrepit old mansion",
//                "The decrepit old mansion stood atop a hill like a silent sentinel watching over the valley below. " +
//                        "Tall windows stared blankly from the walls, their glass panes long since shattered. The grounds were overgrown with weeds and wildflowers, a testament to the fact that no one had set foot here in many years.",
//                "Author1@gmail.com", testCategory1);
//        saveArticle("The silver moon",
//                "The silver moon shone brightly against the night sky, its reflection glittering on the lake’s still " +
//                        "surface below. Fireflies sparkled around me like stars fallen from the heavens, their lights twinkling in tandem with those of distant galaxies.",
//                "Author3@gmail.com", testCategory2);
//        saveArticle("The beach",
//                "The beach was a tranquil paradise, soft white sand stretching out towards an endless blue horizon. " +
//                        "The waves crashed gently against the shore, their foamy spray cooling my skin under the hot afternoon sun.",
//                "Author2@gmail.com", testCategory1);
//        saveArticle("The snow-capped mountain",
//                "The snow-capped mountain peak rose majestically above me, its rocky sides glinting in the bright " +
//                        "sunlight. I could feel the chill of the air around me and see my breath misting in front of me as I trudged up the steep path.",
//                "Author3@gmail.com", testCategory1);
//        saveArticle("The sun was setting",
//                "The sun was setting, painting the sky in vibrant shades of orange, pink and purple. The clouds were " +
//                        "streaked with golden light, completing the brilliant spectacle that was taking place all around me.",
//                "Author1@gmail.com", testCategory2);
//    }
//
//    /**
//     * Save user with email, password and nickname
//     *
//     * @param email The email of user
//     * @param password The password of user
//     * @param nickname The nickname of user
//     */
//    private void saveUser(String email, String password, String nickname) {
//        if (!userService.isDuplicatedEmail(email)) {
//            // Create a new user and save it to the database
//            userService.save(new AddUserRequest(email, password, nickname));
//        }
//    }
//
//    /**
//     * Save article with title, content and email of user
//     *
//     * @param title The title of article
//     * @param content The content of article
//     * @param email The email of writer
//     */
//    private void saveArticle(String title, String content, String email, Category category) {
//        if (userService.existsByEmail(email)) {
//            articleRepository.save(Article.builder()
//                    .title(title)
//                    .content(content)
//                    .author(userService.getUserByEmail(email))
//                    .category(category)
//                    .build());
//        }
//    }
//
//    private Category saveCategory(String parent, String name) {
//        return categoryService.createCategory(new CreateCategoryRequest((parent == null) ? name : parent + "_" + name));
//    }
//}
