package com.library.IService;

import com.library.dto.LoginUserDto;
import com.library.dto.RegisterUserDto;
import com.library.model.User;

public interface AuthenticationService {
    User signup(RegisterUserDto input);
    User authenticate(LoginUserDto input);
}
