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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
            power.setCost(powerCreateUsageRequestDto.getUsageAmount());
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

        List<Power> powers = powerRepository.findAllByUserAndYearBetweenAndMonthBetween(
                user,
                currentYear - 1, currentYear, // 작년과 올해
                1, currentMonth // 1월 ~ 이번 달
        );

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
}
