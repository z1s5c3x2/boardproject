package com.application.common.util.jwtService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtils {
    public static String contextGetUserEmail(){
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
