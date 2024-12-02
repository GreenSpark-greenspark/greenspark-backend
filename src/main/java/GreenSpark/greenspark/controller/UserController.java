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
    public DataResponseDto<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            customLogoutHandler.logout(request, response);
            return DataResponseDto.of(null, "로그아웃이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return DataResponseDto.of("로그아웃 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/users/info")
    public DataResponseDto<?> CreateUserInfo(@CookieValue("access") String authorization, @RequestBody UserInfoDto userInfoDto){
        try{
            long userId=getUserId(authorization);
            userService.InputUserInfo(userId, userInfoDto);
            return DataResponseDto.of(null,"부양가족수/납부일이 입력되었습니다.");
        } catch(IllegalStateException e){
            return DataResponseDto.of(e.getMessage());
        }
    }

    @PatchMapping("/users/mod-info")
    public DataResponseDto<?> UpdateUserInfo(@CookieValue("access") String authorization, @RequestBody UserInfoDto userInfoDto){
        try{
            long userId=getUserId(authorization);
            userService.updateUserInfo(userId, userInfoDto);
            return DataResponseDto.of(null,"부양가족수/납부일이 변경되었습니다.");
        } catch(IllegalStateException e){
            return DataResponseDto.of(e.getMessage());
        }
    }
    @GetMapping("/users/verify")
    public DataResponseDto<?> verifyUser(@CookieValue("access") String authorization) {
        try {
            // 토큰에서 유저 정보를 추출
            long userId = getUserId(authorization);

            // 유저가 DB에 존재하는지 확인
            if (userRepository.existsById(userId)) {
                return DataResponseDto.of(null, "유저가 확인되었습니다.");
            } else {
                return DataResponseDto.of("유저를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return DataResponseDto.of("토큰 검증에 실패했습니다.");
        }
    }

    private long getUserId(String authorizaiton){
        String token=authorizaiton.replace("Bearer ","");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }

}
