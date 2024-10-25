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
                .orElseThrow(() -> new EntityNotFoundException("저번 달 요금 데이터를 찾을 수 없습니다."));

        if (lastMonthPower.getCost() == 0) {
            throw new IllegalArgumentException("저번 달 요금 데이터가 존재하지 않습니다.");
        }

        Power monthBeforeLastPower = powerRepository.findByUserAndYearAndMonth(user, monthBeforeLastYear, monthBeforeLast)
                .orElseThrow(() -> new EntityNotFoundException("저저번 달 요금 데이터를 찾을 수 없습니다."));

        if (monthBeforeLastPower.getCost() == 0) {
            throw new IllegalArgumentException("저저번 달 요금 데이터가 존재하지 않습니다.");
        }

        int lastMonthCost = lastMonthPower.getCost();
        int monthBeforeLastCost = monthBeforeLastPower.getCost();
        int costDifference = lastMonthCost - monthBeforeLastCost;

        return PowerConverter.toPowerGetLastMonthPowerResponseDto(lastMonthCost, costDifference);
    }
}
