package GreenSpark.greenspark.OAuth;

import GreenSpark.greenspark.domain.Refresh;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        String username = oauthUser.getUsername();
        String role = oauthUser.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createJwt("access",username, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh",username, role, 1209600000L);
        addRefreshEntity(username,refreshToken,1209600000L);

//        createCookie("access",accessToken);
//        createCookie("refresh",refreshToken);

        addCookieWithSameSite(response, "access", accessToken, 600);
        addCookieWithSameSite(response, "refresh", refreshToken, 1209600);

//        response.sendRedirect("https://green-spark.vercel.app/");
        response.sendRedirect("http://localhost:3000/auth/google/callback");
    }

//private void addCookieWithSameSite(HttpServletResponse response, String name, String value, int maxAge) {
//    ResponseCookie cookie = ResponseCookie.from(name, value)
//            .maxAge(maxAge)
//            .httpOnly(true)
//            .secure(true) // HTTPS에서만 전송
//            .sameSite("None") // 크로스 사이트에서 작동 가능
//            .path("/")
//            .build();
//    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//}
private void addCookieWithSameSite(HttpServletResponse response, String name, String value, int maxAge) {
    // Host 헤더에서 localhost 여부 판단
    boolean isLocalhost = "localhost".equals(response.getHeader("Host"));

    ResponseCookie cookie = ResponseCookie.from(name, value)
            .maxAge(maxAge) // 쿠키의 유효기간 설정 (초 단위)
            .httpOnly(true) // 클라이언트 스크립트에서 접근 불가
            .secure(!isLocalhost) // localhost에서는 secure=false, 배포 환경에서는 true
            .sameSite(isLocalhost ? "Lax" : "None") // 개발 환경에서는 Lax, 배포 환경에서는 None
            .domain(isLocalhost ? null : "api.greenspark.shop") // localhost에서는 도메인 제거, 배포 환경에서는 도메인 지정
            .path("/") // 모든 경로에서 접근 가능
            .build();

    // Set-Cookie 헤더에 추가
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
}

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        // 이미 해당 username에 대한 refresh token이 DB에 존재하는지 확인
        Optional<Refresh> existingRefreshToken = refreshRepository.findByUsername(username);

        // 기존 Refresh Token이 있으면 삭제
        existingRefreshToken.ifPresent(refreshRepository::delete);

        // 현재 시간에 expiredMs 밀리초를 더해서 LocalDateTime 생성
        LocalDateTime expirationDate = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));

        // 새 Refresh Token 엔티티 생성
        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expirationDate);

        // 새 Refresh 엔티티 저장
        refreshRepository.save(refreshEntity);
    }
}
//response.sendRedirect("http://localhost:3000/auth/google/callback");
//private void addCookieWithSameSite(HttpServletResponse response, String name, String value, int maxAge) {
//    // Host 헤더에서 localhost 여부 판단
//    boolean isLocalhost = "localhost".equals(response.getHeader("Host"));
//
//    ResponseCookie cookie = ResponseCookie.from(name, value)
//            .maxAge(maxAge) // 쿠키의 유효기간 설정 (초 단위)
//            .httpOnly(true) // 클라이언트 스크립트에서 접근 불가
//            .secure(!isLocalhost) // localhost에서는 secure=false, 배포 환경에서는 true
//            .sameSite(isLocalhost ? "Lax" : "None") // 개발 환경에서는 Lax, 배포 환경에서는 None
//            .domain(isLocalhost ? null : "api.greenspark.shop") // localhost에서는 도메인 제거, 배포 환경에서는 도메인 지정
//            .path("/") // 모든 경로에서 접근 가능
//            .build();
//
//    // Set-Cookie 헤더에 추가
//    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//}