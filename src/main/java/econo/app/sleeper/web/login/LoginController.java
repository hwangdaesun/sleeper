package econo.app.sleeper.web.login;

import econo.app.sleeper.service.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "login", description = "로그인 관련 API")
public class LoginController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "api simple explain", description = "api specific explain")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    // 로그아웃은 refresh토큰을 넣은 쿠키를 없애줘야한다.

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        LoginResponse loginResponse = loginService.login(loginRequest, request);
        String userId=loginRequest.getUserId();

        if (loginResponse.getAccessToken().isBlank()) {
            return new ResponseEntity<>(loginResponse, HttpStatus.FORBIDDEN);
        }
        if (loginResponse.getRefreshToken().isBlank()) {
            return new ResponseEntity<>(loginResponse, HttpStatus.FORBIDDEN);
        }
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        response.addHeader("authorization", accessToken);
        response.addCookie(cookie);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    //Body에는 쿠키가 없다.
    @PostMapping("/logout")
    public ResponseEntity<Void>logout( HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        Cookie nullCookie = loginService.logout(cookie);
        response.addCookie(nullCookie);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
