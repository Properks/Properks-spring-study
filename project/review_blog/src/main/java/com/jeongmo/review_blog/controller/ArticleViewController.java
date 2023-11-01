package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.Article;
import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.article_view.ArticleViewResponse;
import com.jeongmo.review_blog.service.ArticleService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {

    private final ArticleService articleService;
    private static final String MAIN = "mainPage";

    /**
     * @// TODO: 2023/10/28 Give page number and article size to frontend, make size parameter
     */
    @GetMapping("/home")
    public String mainPage(@RequestParam(required = false) Integer page, Model model, Authentication authentication) {
        int size = 10;
        List<ArticleViewResponse> articles = new java.util.ArrayList<>(articleService.getArticles()
                .stream().map(ArticleViewResponse::new)
                .toList());

        checkAndAddLoginInfo(model, authentication);

        Collections.reverse(articles);
        if (page == null) {page = 1;}
        model.addAttribute("articles", articles
                .subList((page - 1) * 10, Math.min(page * 10, articles.size())));

        model.addAttribute("totalArticleSize", articles.size());
        model.addAttribute("totalPage", (articles.size() - 1) / size + 1);
        model.addAttribute("currentPage", page);
        return MAIN;
    }

    @GetMapping("/article/{id}")
    public String viewArticle(Model model, @PathVariable Long id, Authentication authentication) {
        ArticleViewResponse response = new ArticleViewResponse(articleService.getArticle(id));
        checkAndAddLoginInfo(model, authentication);
        model.addAttribute("viewArticle", response);
        return MAIN;
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model, Authentication authentication) {
        if (id == null) {
            model.addAttribute("newArticle", new ArticleViewResponse());
        } else {
            Article foundArticle = articleService.getArticle(id);
            if (foundArticle.getAuthor().getId().equals(((User)getAuthentication().getPrincipal()).getId())) {
                model.addAttribute("newArticle", new ArticleViewResponse(articleService.getArticle(id)));
            } else {
                model.addAttribute("errorMessage", "401 ERROR, You're not writer.");
                return viewArticle(model, id, authentication);
            }
        }
        checkAndAddLoginInfo(model, authentication);
        return MAIN;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void checkAndAddLoginInfo(Model model, Authentication authentication) {
        if (authentication!= null && getAuthentication().isAuthenticated()) {
            User user = (User) getAuthentication().getPrincipal();
            model.addAttribute("loginIn", user);
        }
    }
}