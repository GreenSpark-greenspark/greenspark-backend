package GreenSpark.greenspark.service;

import GreenSpark.greenspark.converter.PowerConverter;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PowerRequestDto;
import GreenSpark.greenspark.repository.PowerRepository;
import GreenSpark.greenspark.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PowerService {

    private final PowerRepository powerRepository;
    private final UserRepository userRepository;

    public Power createPower(Long userId, PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Power power = PowerConverter.toPower(user, powerCreateRequestDto);

        return powerRepository.save(power);
    }
}
