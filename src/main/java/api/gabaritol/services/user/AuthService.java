package api.gabaritol.services.user;

import api.gabaritol.DTOs.user.ResponseLoginUserDTO;

public interface AuthService {
    ResponseLoginUserDTO loginWithCode(String email, String code);
    
    ResponseLoginUserDTO loginWithPassword(String email, String password);

    ResponseLoginUserDTO register(String email);
}
