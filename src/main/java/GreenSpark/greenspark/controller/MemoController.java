package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.domain.Memo;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.MemoDto;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.AppliancesRepository;
import GreenSpark.greenspark.repository.MemoRepository;
import GreenSpark.greenspark.repository.UserRepository;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.AppliancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final AppliancesRepository appliancesRepository;
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    // 메모 작성 API
    @PostMapping("/appliances/{applianceId}/memo")
    public DataResponseDto<?> createMemo(@CookieValue("access") String authorization,
                                         @PathVariable Long applianceId,
                                         @RequestBody MemoDto memoDto) {
        try {
            long userId = getUserId(authorization);

            Appliance appliance = appliancesRepository.findById(applianceId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 가전제품을 찾을 수 없습니다."));

            if (!appliance.getUser().getUserId().equals(userId)) {
                return DataResponseDto.of(null, "해당 가전제품에 접근 권한이 없습니다.");
            }

            Memo memo = Memo.builder()
                    .content(memoDto.getContent())
                    .user(appliance.getUser())
                    .appliance(appliance)
                    .build();

            memoRepository.save(memo);

            return DataResponseDto.of(null, "메모가 성공적으로 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            return DataResponseDto.of(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "메모 추가 중 오류가 발생했습니다.");
        }
    }

    private long getUserId(String authorization){
        String token=authorization.replace("Bearer ","");
        String email = jwtUtil.getemail(token);
        Optional<User> user = userRepository.findByEmail(email);
        return user.get().getUserId();
    }
}