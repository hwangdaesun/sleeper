package econo.app.sleeper.web.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse implements Serializable{

    private String message;
    private Long sleepPk;
    private Long userPk;
    private Long userSleepPk;
    public static LoginResponse of (String message, Long sleepPk, Long userPk, Long userSleepPk){
        return LoginResponse.builder()
                .sleepPk(sleepPk)
                .message(message)
                .userPk(userPk)
                .userSleepPk(userSleepPk)
                .build();
    }



}
