package GreenSpark.greenspark.service;

import GreenSpark.greenspark.converter.PowerConverter;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PowerRequestDto;
import GreenSpark.greenspark.dto.PowerResponseDto;
import GreenSpark.greenspark.repository.PowerRepository;
import GreenSpark.greenspark.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PowerService {

    private final PowerRepository powerRepository;
    private final UserRepository userRepository;

    public Power createCostPower(Long userId, PowerRequestDto.PowerCreateCostRequestDto powerCreateCostRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        int year = powerCreateCostRequestDto.getYear();
        int month = powerCreateCostRequestDto.getMonth();
        Optional<Power> existingPower = powerRepository.findByYearAndMonthAndUser(year, month, user);

        Power power;
        if (existingPower.isPresent()) { // 기존 데이터가 있을 경우 업데이트
            power = existingPower.get();
            power.setCost(powerCreateCostRequestDto.getCost());
        } else { // 기존 데이터가 없을 경우 새로 생성
            power = PowerConverter.CostRequestDtotoPower(user, powerCreateCostRequestDto);
        }

        return powerRepository.save(power);
    }

    public Power createUsagePower(Long userId, PowerRequestDto.PowerCreateUsageRequestDto powerCreateUsageRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        int year = powerCreateUsageRequestDto.getYear();
        int month = powerCreateUsageRequestDto.getMonth();
        Optional<Power> existingPower = powerRepository.findByYearAndMonthAndUser(year, month, user);

        Power power;
        if (existingPower.isPresent()) { // 기존 데이터가 있을 경우 업데이트
            power = existingPower.get();
            power.setUsageAmount(powerCreateUsageRequestDto.getUsageAmount());
        } else { // 기존 데이터가 없을 경우 새로 생성
            power = PowerConverter.UsageRequestDtotoPower(user, powerCreateUsageRequestDto);
        }

        return powerRepository.save(power);
    }

    public List<PowerResponseDto.PowerGetDataResponseDto> getPowerData(Long userId, String display) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // 최근 24개월 전 날짜 계산
        LocalDate twoYearsAgo = now.minusMonths(24);

        // 데이터베이스에서 해당 사용자의 모든 데이터 가져오기
        List<Power> allPowers = powerRepository.findAllByUser(user);

        // 최근 2년 이내의 데이터만 필터링
        List<Power> powers = allPowers.stream()
                .filter(power -> {
                    LocalDate powerDate = LocalDate.of(power.getYear(), power.getMonth(), 1);
                    return (powerDate.isAfter(twoYearsAgo) || powerDate.isEqual(twoYearsAgo)) && powerDate.isBefore(now.plusMonths(1));
                })
                .collect(Collectors.toList());

        if ("bill".equalsIgnoreCase(display)) {
            return powers.stream()
                    .map(power -> new PowerResponseDto.PowerGetDataResponseDto(power.getYear(), power.getMonth(), power.getCost()))
                    .collect(Collectors.toList());
        } else if ("usage".equalsIgnoreCase(display)) {
            return powers.stream()
                    .map(power -> new PowerResponseDto.PowerGetDataResponseDto(power.getYear(), power.getMonth(), power.getUsageAmount()))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("알맞은 display 문자열이 아닙니다.: " + display);
        }
    }

    public List<PowerResponseDto.PowerGetAllResponseDto> getAllPowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<Power> powers = powerRepository.findByUser(user);
        return powers.stream()
                .map(power -> new PowerResponseDto.PowerGetAllResponseDto(power.getYear(), power.getMonth(), power.getCost(), power.getUsageAmount()))
                .collect(Collectors.toList());
    }

    public Power createPower(Long userId, PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        int year = powerCreateRequestDto.getYear();
        int month = powerCreateRequestDto.getMonth();
        Optional<Power> existingPower = powerRepository.findByYearAndMonthAndUser(year, month, user);

        Power power;
        if (existingPower.isPresent()) { // 기존 데이터가 있을 경우 업데이트
            power = existingPower.get();
            power.setCost(powerCreateRequestDto.getCost());
            power.setUsageAmount(powerCreateRequestDto.getUsageAmount());
        } else { // 기존 데이터가 없을 경우 새로 생성
            power = PowerConverter.toPower(user, powerCreateRequestDto);
        }

        return powerRepository.save(power);
    }

    public PowerResponseDto.PowerGetLastMonthPowerResponseDto getLastMonthPower(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        int lastMonthYear = currentMonth == 1 ? currentYear - 1 : currentYear; // 1월이면 저번달의 년도를 올해 - 1 아니면 그대로
        int lastMonth = currentMonth == 1 ? 12 : currentMonth - 1; // 1월이면 저번달은 12월 아니면 -1

        int monthBeforeLastYear = lastMonth == 1 ? lastMonthYear - 1 : lastMonthYear; // 저번달이 1월이면 저저번달의 년도를 올해 -1 아니면 그대로
        int monthBeforeLast = lastMonth == 1 ? 12 : lastMonth - 1; // 저번달이 1월이면 저저번달은 12월

        Power lastMonthPower = powerRepository.findByUserAndYearAndMonth(user, lastMonthYear, lastMonth)
                .orElseThrow(() -> new EntityNotFoundException("저번 달 파워를 찾을 수 없습니다."));

        Power monthBeforeLastPower = powerRepository.findByUserAndYearAndMonth(user, monthBeforeLastYear, monthBeforeLast)
                .orElseThrow(() -> new EntityNotFoundException("저저번 달 파워를 찾을 수 없습니다."));

        int lastMonthCost = lastMonthPower.getCost();
        int monthBeforeLastCost = monthBeforeLastPower.getCost();

        return PowerConverter.toPowerGetLastMonthPowerResponseDto(lastMonthCost, monthBeforeLastCost);
    }

    public PowerResponseDto.PowerGetExpectedCostResponseDto getExpectedCost(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        int lastMonthYear = currentMonth == 1 ? currentYear - 1 : currentYear; // 1월이면 저번달의 년도를 올해 - 1 아니면 그대로
        int lastMonth = currentMonth == 1 ? 12 : currentMonth - 1; // 1월이면 저번달은 12월 아니면 -1

        Power lastMonthPower = powerRepository.findByUserAndYearAndMonth(user, lastMonthYear, lastMonth)
                .orElseThrow(() -> new EntityNotFoundException("저번 달 파워를 찾을 수 없습니다."));

        int lastMonthCost = lastMonthPower.getCost();

        List<Power> userPowers = powerRepository.findAllByUser(user);

        // FastAPI에 보낼 JSON request 생성
        List<PowerRequestDto.PowerGetExpectedCostRequestToFastAPIDto> expectedCostRequest = userPowers.stream()
                // cost가 0이 아닌 경우만 필터링 (cost가 0인 경우는 전력사용량만 입력한 경우이므로)
                .filter(power -> power.getCost() != 0)
                .map(power -> new PowerRequestDto.PowerGetExpectedCostRequestToFastAPIDto(
                        power.getYear() + "-" + String.format("%02d", power.getMonth()) + "-01", // "yyyy-MM-dd" 형식의 문자열
                        power.getCost()
                ))
                .toList();

        // FastAPI와 통신하기 위한 세팅
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<PowerRequestDto.PowerGetExpectedCostRequestToFastAPIDto>> requestEntity = new HttpEntity<>(expectedCostRequest, headers);

        String url = "http://172.31.15.13:8000/ml"; // FastAPI 서버 URL 확인
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

        int expectedCost = ((Number) response.getBody().get("predicted_cost")).intValue();

        return PowerConverter.toGetExpectedCostResponseDto(expectedCost, lastMonthCost);
    }

    // 매년 1월 1일 00:00에 실행되도록 스케줄링 설정
    @Scheduled(cron = "0 0 0 1 1 *")
    public void deleteOldRecordsOnNewYear() {
        int thresholdYear = LocalDate.now().getYear() - 3;
        powerRepository.deleteByYearLessThanEqual(thresholdYear);
        System.out.println("3년 이상 지난 데이터가 삭제되었습니다.");
    }
}
