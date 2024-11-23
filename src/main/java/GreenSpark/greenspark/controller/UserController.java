package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.OAuth.CustomLogoutHandler;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.UserInfoDto;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.UserRepository;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CustomLogoutHandler customLogoutHandler;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        // Google OAuth2 로그인으로 리다이렉트
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        customLogoutHandler.logout(request, response);
    }

    @PostMapping("/users/info/")
    public DataResponseDto<?> CreateUserInfo(@CookieValue("access") String authorization, @RequestBody UserInfoDto userInfoDto){
        try{
            long userId=getUserId(authorization);
            userService.InputUserInfo(userId, userInfoDto);
            return DataResponseDto.of(null,"부양가족수/납부일이 입력되었습니다.");
        } catch(IllegalStateException e){
            return DataResponseDto.of(e.getMessage());
        }
    }

    @PatchMapping("/users/mod-info/")
    public DataResponseDto<?> UpdateUserInfo(@CookieValue("access") String authorization, @RequestBody UserInfoDto userInfoDto){
        try{
            long userId=getUserId(authorization);
            userService.updateUserInfo(userId, userInfoDto);
            return DataResponseDto.of(null,"부양가족수/납부일이 변경되었습니다.");
        } catch(IllegalStateException e){
            return DataResponseDto.of(e.getMessage());
        }
    }

    private long getUserId(String authorizaiton){
        String token=authorizaiton.replace("Bearer ","");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }

}
