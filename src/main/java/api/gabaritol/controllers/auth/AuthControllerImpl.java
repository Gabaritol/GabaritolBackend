package api.gabaritol.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.gabaritol.DTOs.user.LoginCodeRequestDTO;
import api.gabaritol.DTOs.user.LoginPasswordRequestDTO;
import api.gabaritol.DTOs.user.RegisterRequestDTO;
import api.gabaritol.DTOs.user.ResponseLoginUserDTO;
import api.gabaritol.DTOs.user.VerifyCodeRequestDTO;
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
    public ResponseEntity<ResponseLoginUserDTO> loginWithCode(VerifyCodeRequestDTO request) {
        ResponseLoginUserDTO response = authService.loginWithCode(request.email(), request.code());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<ResponseLoginUserDTO> loginWithPassword(LoginPasswordRequestDTO request) {
        ResponseLoginUserDTO response = authService.loginWithPassword(request.email(), request.password());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> requestLoginCode(LoginCodeRequestDTO request) {
        userService.requestLoginCode(request.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
