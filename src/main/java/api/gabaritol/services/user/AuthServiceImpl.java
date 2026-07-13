package api.gabaritol.services.user;

import org.springframework.stereotype.Service;

import api.gabaritol.DTOs.user.ResponseLoginUserDTO;
import api.gabaritol.entities.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService service;
    private final JwtService jwtService;

    @Override
    public ResponseLoginUserDTO loginWithCode(String email, String code) {
        User user = service.verifyCode(email, code);
        String token = jwtService.generateToken(user);
        return new ResponseLoginUserDTO(token);
    }

    @Override
    public ResponseLoginUserDTO loginWithPassword(String email, String password) {
        User user = service.loginWithPassword(email, password);
        String token = jwtService.generateToken(user);
        return new ResponseLoginUserDTO(token);
    }

    @Override
    public ResponseLoginUserDTO register(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }
    
}
