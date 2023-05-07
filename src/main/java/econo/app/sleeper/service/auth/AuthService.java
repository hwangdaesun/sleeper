package econo.app.sleeper.service.auth;

import econo.app.sleeper.domain.Auth.Auth;
import econo.app.sleeper.domain.Auth.AuthRepository;
import econo.app.sleeper.domain.member.Member;
import econo.app.sleeper.domain.member.MemberRepository;
import econo.app.sleeper.web.login.JwtTokenProvider;
import econo.app.sleeper.web.login.LoginRequest;
import econo.app.sleeper.web.login.LoginTokenDto;
import econo.app.sleeper.web.login.ReissueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    public LoginTokenDto login(LoginRequest loginRequest) {
        String loginId = loginRequest.getMemberId();
        Member member = memberRepository.findById(loginId).orElseThrow(() -> new IllegalArgumentException("아이디가 일치하지 않습니다."));
        if (!member.getMemberPassword().equals(loginRequest.getMemberPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        LoginTokenDto loginTokenDto = generateToken(loginId);
        Auth auth = Auth.createAuth(loginTokenDto.getAccessToken(),loginTokenDto.getRefreshToken());
        authRepository.save(auth);
        return loginTokenDto;
    }

    public LoginTokenDto generateToken(String loginId){
        String newAccessToken = jwtTokenProvider.createAccessToken(loginId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(loginId);
        LoginTokenDto loginTokenDto = LoginTokenDto.of(newAccessToken, newRefreshToken);
        return loginTokenDto;
    }

    public boolean isEqualToRefreshToken(String accessToken, String refreshToken){
        String recentRefreshToken = authRepository.findRecentRefreshToken(accessToken);
        if(refreshToken.equals(recentRefreshToken)){
            return true;
        }
        return false;
    }


    public Cookie logout(Cookie cookie) {
        cookie.setValue(null);
        cookie.setMaxAge(0);
        return cookie;
    }
}



