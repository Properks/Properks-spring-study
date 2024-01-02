package com.jeongmo.review_blog.controller;

import com.jeongmo.review_blog.domain.User;
import com.jeongmo.review_blog.dto.user.AddUserRequest;
import com.jeongmo.review_blog.dto.user.UpdateAccountNickname;
import com.jeongmo.review_blog.dto.user.UpdateAccountPassword;
import com.jeongmo.review_blog.dto.user.UserResponse;
import com.jeongmo.review_blog.service.ArticleService;
import com.jeongmo.review_blog.service.UserService;
import com.jeongmo.review_blog.util.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;


@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final ArticleService articleService;
    private final SecurityUtils securityUtils;

    @PostMapping("/user")
    public String signup(@RequestParam("email") String email, @RequestParam("password") String password,
                         @RequestParam("nickname") String nickname) {
        userService.save(new AddUserRequest(email, password, nickname));
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request,
    HttpServletResponse response, @PathVariable Long userId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId().equals(userId)) {
            articleService.deleteArticlesByAuthorId(user.getId());
            //logout
            SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
            handler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            //delete account
            userService.delete(userId);
        }
        // redirect in frontend.
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/nickname")
    public ResponseEntity<UserResponse> updateAccountNickname(@RequestBody UpdateAccountNickname nicknameDto) {
        User user = userService.updateNickname(nicknameDto);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
            // Cannot update because nickname already exists. (409 error)
        }
        securityUtils.updateUserInfo(user);
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @PutMapping("/user/password")
    public ResponseEntity<UserResponse> updateAccountPassword(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              @RequestBody UpdateAccountPassword passwordDto) throws Exception{
        User user = userService.updatePassword(passwordDto);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        logout(request, response);
        // Redirect login page in frontend
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @GetMapping("/api/email/{email}")
    public ResponseEntity<Void> validEmail(@PathVariable String email) {
        if (userService.isDuplicatedEmail(email)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}
