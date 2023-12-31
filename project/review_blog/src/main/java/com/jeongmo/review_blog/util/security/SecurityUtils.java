package com.jeongmo.review_blog.util.security;

import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.user.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SecurityUtils {
    /**
     * Get Authentication.
     *
     * @return The Authentication (SecurityContextHolder.getContext().getAuthentication();)
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Check and add authentication information to model
     *
     */
    public void checkAndAddLoginInfo(Model model, Authentication authentication) {
        if (authentication!= null && getAuthentication().isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("loginIn", new UserResponse(user));
        }
    }

}
