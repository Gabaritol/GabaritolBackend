package api.gabaritol.services.auth;

import api.gabaritol.DTOs.user.ResponseLoginUserDTO;

public interface AuthService {
    ResponseLoginUserDTO loginWithCode(String email, String code);
    
    ResponseLoginUserDTO loginWithPassword(String email, String password);
}
