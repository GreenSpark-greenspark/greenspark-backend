package GreenSpark.greenspark.service;

import GreenSpark.greenspark.OAuth.CustomOAuth2User;
import GreenSpark.greenspark.dto.GoogleResponse;
import GreenSpark.greenspark.dto.OAuth2Response;
import GreenSpark.greenspark.dto.Role;
import GreenSpark.greenspark.dto.UserDTO;
import GreenSpark.greenspark.repository.UserRepository;
import GreenSpark.greenspark.domain.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String username = oAuth2Response.getProvider()+ " "+ oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username);

        if(existData == null) {
            User userEntity = User.builder()
                    .username(username)
                    .role(Role.ROLE_USER)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .attendance(false)
                    .build();

            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);

        }
        else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole().toString());

            return new CustomOAuth2User(userDTO);
        }



    }
}