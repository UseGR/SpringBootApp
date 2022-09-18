package ru.galeev.springcourse.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.galeev.springcourse.security.jwt.JwtUser;

public class SecurityHelper {

    public static JwtUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) authentication.getPrincipal();
    }

}
