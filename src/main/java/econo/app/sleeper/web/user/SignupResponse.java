package econo.app.sleeper.web.user;

import econo.app.sleeper.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SignupResponse {

    private final String userId;
    private final String userNickName;

    public static SignupResponse toDto(User user){
        return SignupResponse.builder()
                .userId(user.getUserId())
                .userNickName(user.getUserNickName())
                .build();
    }

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .userNickName(userNickName)
                .build();
    }

}
