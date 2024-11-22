package GreenSpark.greenspark.service;

import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.UserInfoDto;
import GreenSpark.greenspark.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void InputUserInfo(Long userId, UserInfoDto userInfoDto) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.getHouseholdMembers() == 0 && user.getElectricityDueDate() == 0) {
                user.setHouseholdMembers(userInfoDto.getHouseholdMembers());
                user.setElectricityDueDate(userInfoDto.getElectricityDueDate());
                userRepository.save(user);
            } else {
                throw new IllegalStateException("부양가족 수와 납부일은 처음 로그인 시에만 설정할 수 있습니다.");
            }
        }
    }

    public void updateUserInfo(Long userId, UserInfoDto userInfoDto) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.getHouseholdMembers() != 0) {
                user.setHouseholdMembers(userInfoDto.getHouseholdMembers());
            }
            if (user.getElectricityDueDate() !=0) {
                user.setElectricityDueDate(userInfoDto.getElectricityDueDate());
            }

            userRepository.save(user);
        } else {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
    }
}
