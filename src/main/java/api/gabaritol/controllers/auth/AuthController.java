package api.gabaritol.controllers.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import api.gabaritol.DTOs.user.LoginCodeRequestDTO;
import api.gabaritol.DTOs.user.LoginPasswordRequestDTO;
import api.gabaritol.DTOs.user.RegisterRequestDTO;
import api.gabaritol.DTOs.user.ResponseLoginUserDTO;
import api.gabaritol.DTOs.user.VerifyCodeRequestDTO;
import jakarta.validation.Valid;

@RequestMapping("/api/auth")
public interface AuthController {
    @PostMapping("/register")
    ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request);

    @PostMapping("/login/request-code")
    ResponseEntity<Void> requestLoginCode(@Valid @RequestBody LoginCodeRequestDTO request);

    @PostMapping("/login/code")
    ResponseEntity<ResponseLoginUserDTO> loginWithCode(@Valid @RequestBody VerifyCodeRequestDTO request);

    @PostMapping("/login/password")
    ResponseEntity<ResponseLoginUserDTO> loginWithPassword(@Valid @RequestBody LoginPasswordRequestDTO request);
}