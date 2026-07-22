package api.gabaritol.controllers.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.gabaritol.DTOs.user.*;
import api.gabaritol.services.auth.AuthService;
import api.gabaritol.services.user.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final UserService userService;
    
    @Override
    public ResponseEntity<Void> register(RegisterRequestDTO request) {
        userService.register(request.email());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> loginWithCode(VerifyCodeRequestDTO request) {
        ResponseLoginUserDTO response = authService.loginWithCode(request.email(), request.code());

        ResponseCookie cookie = ResponseCookie.from("auth_token", response.token())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Lax")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    @Override
    public ResponseEntity<Void> loginWithPassword(LoginPasswordRequestDTO request) {
        ResponseLoginUserDTO response = authService.loginWithPassword(request.email(), request.password());
        
        ResponseCookie cookie = ResponseCookie.from("auth_token", response.token())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Lax")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    @Override
    public ResponseEntity<Void> requestLoginCode(LoginCodeRequestDTO request) {
        userService.requestLoginCode(request.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
