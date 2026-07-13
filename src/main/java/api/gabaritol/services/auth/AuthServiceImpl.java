package api.gabaritol.services.auth;

import org.springframework.stereotype.Service;

import api.gabaritol.DTOs.user.ResponseLoginUserDTO;
import api.gabaritol.entities.user.User;
import api.gabaritol.services.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public ResponseLoginUserDTO loginWithCode(String email, String code) {
        User user = userService.verifyCode(email, code);
        String token = tokenService.generateToken(user);
        return new ResponseLoginUserDTO(token);
    }

    @Override
    public ResponseLoginUserDTO loginWithPassword(String email, String password) {
        User user = userService.loginWithPassword(email, password);
        String token = tokenService.generateToken(user);
        return new ResponseLoginUserDTO(token);
    }
}
