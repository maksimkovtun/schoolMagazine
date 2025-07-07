package com.project.schoolmagazine.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserContextHelper {
    private static Authentication auth;

    public Boolean isAuth(){
        auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);
    }
    public String getUserName() {
        if (isAuth()) {
            User user = (User) auth.getPrincipal();
            return user.getUsername();
        }
        return null;
    }

    Map<String, Boolean> getUserRoles() {
        Map<String, Boolean> roles = new HashMap<>();
        roles.put("isAdmin", false);
        roles.put("isHTeacher", false);
        roles.put("isTeacher", false);
        roles.put("isStudent", false);
        roles.put("isUser", false);
        if (isAuth()) {
            for (var auth : auth.getAuthorities()) {
                String role = auth.getAuthority();
                switch (role) {
                    case "ROLE_ADMIN" -> {
                        roles.put("isAdmin", true);
                        roles.put("isHTeacher", true);
                        roles.put("isTeacher", true);
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_HEAD_TEACHER" -> {
                        roles.put("isHTeacher", true);
                        roles.put("isTeacher", true);
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_TEACHER" -> {
                        roles.put("isTeacher", true);
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_STUDENT" -> {
                        roles.put("isStudent", true);
                        roles.put("isUser", true);
                    }
                    case "ROLE_USER" -> roles.put("isUser", true);
                }
            }
        }
        return roles;
    }
}
