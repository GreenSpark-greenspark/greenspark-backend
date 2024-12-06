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
    public DataResponseDto<?> createOrUpdateMemo(@CookieValue("access") String authorization,
                                                 @PathVariable Long applianceId,
                                                 @RequestBody MemoDto memoDto) {
        try {
            long userId = getUserId(authorization);

            Appliance appliance = appliancesRepository.findById(applianceId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 가전제품을 찾을 수 없습니다."));

            if (!appliance.getUser().getUserId().equals(userId)) {
                return DataResponseDto.of(null, "해당 가전제품에 접근 권한이 없습니다.");
            }

            // 동일 사용자와 가전제품에 대한 기존 메모 확인
            Memo existingMemo = memoRepository.findByApplianceAndUser(appliance, appliance.getUser());

            if (existingMemo != null) {
                // 기존 메모 업데이트
                existingMemo.setContent(memoDto.getContent());
                memoRepository.save(existingMemo);
                return DataResponseDto.of(null, "메모가 성공적으로 업데이트되었습니다.");
            } else {
                // 새로운 메모 생성
                Memo newMemo = Memo.builder()
                        .content(memoDto.getContent())
                        .user(appliance.getUser())
                        .appliance(appliance)
                        .build();
                memoRepository.save(newMemo);
                return DataResponseDto.of(null, "메모가 성공적으로 추가되었습니다.");
            }
        } catch (IllegalArgumentException e) {
            return DataResponseDto.of(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "메모 처리 중 오류가 발생했습니다.");
        }
    }

    private long getUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }
}