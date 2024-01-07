package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.article_view.ArticleViewResponse;
import com.jeongmo.review_blog.dto.category.CategoryResponse;
import com.jeongmo.review_blog.service.ArticleService;
import com.jeongmo.review_blog.service.CategoryService;
import com.jeongmo.review_blog.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final SecurityUtils securityUtils;

    private static final String MAIN = "mainPage";

    /**
     * Homepage request mapping. (Article list page)
     *
     * @param page The page of home page
     * @param size The size of articles is shown on page
     * @param model The model for thymeleaf
     * @param authentication The authentication is got from spring security
     */
    @GetMapping("/home")
    public String mainPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "10") Integer size,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) String nickname,
                           Model model,
                           Authentication authentication) {
        // Set categories
        addAllCategory(model);

        // Get articles
        List<ArticleViewResponse> articles;
        if (nickname != null){
            articles = new ArrayList<>(articleService.getArticleByUser(nickname)
                    .stream()
                    .map(ArticleViewResponse::new)
                    .toList());
            if (categoryId != null) {
                articles = new ArrayList<>(articles
                        .stream()
                        .filter(article -> article.getCategory().getId().equals(categoryId))
                        .toList());
            }
        }
        else if (categoryId != null) {
            articles = new ArrayList<>(articleService.getArticlesByCategory(categoryId)
                    .stream()
                    .map(ArticleViewResponse::new)
                    .toList());
        }
        else {
            articles = new ArrayList<>(articleService.getArticles()
                    .stream()
                    .map(ArticleViewResponse::new)
                    .toList());
        }

        securityUtils.checkAndAddLoginInfo(model, authentication);

        // Set article
        Collections.reverse(articles);
        model.addAttribute("articles", articles
                .subList((page - 1) * size, Math.min(page * size, articles.size())));

        // Set page
        model.addAttribute("totalArticleSize", articles.size());
        model.addAttribute("totalPage", (articles.size() - 1) / size + 1);
        model.addAttribute("currentPage", page);
        return MAIN;
    }

    @GetMapping("/article/{id}")
    public String viewArticle(Model model, @PathVariable Long id, Authentication authentication) {
        // Set categories
        addAllCategory(model);

        ArticleViewResponse response = new ArticleViewResponse(articleService.getArticle(id));
        securityUtils.checkAndAddLoginInfo(model, authentication);
        model.addAttribute("viewArticle", response);
        return MAIN;
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model,
                             RedirectAttributes attributes, Authentication authentication) {
        // Set categories
        addAllCategory(model);

        if (id == null) {
            if (model.getAttribute("categories") == null) {
                attributes.addFlashAttribute("error", "404 ERROR, Category doesn't exist");
                // addFlashAttribute in RedirectAttributes: input something like model.addAttribute in redirect url.
                return "redirect:/home";
            }
            model.addAttribute("newArticle", new ArticleViewResponse());
        } else {
            Article foundArticle = articleService.getArticle(id);
            if (foundArticle.getAuthor().getId().equals(((User)authentication.getPrincipal()).getId())) {
                model.addAttribute("newArticle", new ArticleViewResponse(articleService.getArticle(id)));
            } else {
                attributes.addFlashAttribute("error", "401 ERROR, You're not writer.");
                return "redirect:/article/" + id;
            }
        }
        securityUtils.checkAndAddLoginInfo(model, authentication);
        return MAIN;
    }

    /**
     * Add all category to model.
     */
    private void addAllCategory(Model model) {
        if (!categoryService.isEmpty()) {
            model.addAttribute("categories",
                    categoryService.findAllCategory()
                            .stream()
                            .map(CategoryResponse::new)
                            .toList());
        }
    }
}