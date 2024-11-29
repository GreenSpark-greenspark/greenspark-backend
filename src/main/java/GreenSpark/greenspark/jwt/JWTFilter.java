package GreenSpark.greenspark.jwt;

import GreenSpark.greenspark.OAuth.CustomOAuth2User;
//import GreenSpark.greenspark.dto.CustomUserDetails;
import GreenSpark.greenspark.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String access = null;
        String refresh= null;
        Cookie[] cookies = request.getCookies();
        String requestUri = request.getRequestURI();

        if (isLoginOrOAuthRequest(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (cookies != null) {
            System.out.println("Cookies received for URI: " + requestUri);
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName());
                if (cookie.getName().equals("access")) {
                    access = cookie.getValue();
                    System.out.println("Access token found: " + access);
                }
                if(cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    System.out.println("Refresh token found: " + refresh);
                }
            }
        }

        if (access == null) {
            System.out.println("Token null for URI: " + requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isExpired(access)) {
            System.out.println("Token expired for URI: " + requestUri);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
            response.getWriter().write("Access token expired");
            filterChain.doFilter(request, response);
            return;
        }


        String username = jwtUtil.getUsername(access);
        String role = jwtUtil.getRole(access);
        System.out.println("username: " + username + ", role: " + role);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isLoginOrOAuthRequest(String requestUri) {
        return requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$");
    }


}