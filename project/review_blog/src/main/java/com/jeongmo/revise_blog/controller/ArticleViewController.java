package com.jeongmo.revise_blog.controller;

import com.jeongmo.revise_blog.domain.User;
import com.jeongmo.revise_blog.dto.article_view.ArticleViewResponse;
import com.jeongmo.revise_blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {

    private final ArticleService articleService;
    private static final String MAIN = "mainPage";

    @GetMapping("/home")
    public String mainPage(Model model, Authentication authentication) {
        List<ArticleViewResponse> articles = articleService.getArticles()
                .stream().map(ArticleViewResponse::new)
                .toList();
        checkAndAddLoginInfo(model, authentication);
        model.addAttribute("articles", articles);
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
        checkAndAddLoginInfo(model, authentication);
        if (id != null) {
            model.addAttribute("newArticle", new ArticleViewResponse(articleService.getArticle(id)));
        } else {
            model.addAttribute("newArticle", new ArticleViewResponse());
        }
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