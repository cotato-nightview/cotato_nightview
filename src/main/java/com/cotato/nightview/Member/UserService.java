package com.cotato.nightview.Member;

import org.springframework.http.ResponseEntity;

public interface UserService {
    //ResponseEntity saveUser(UserDto userDto);

    //ResponseEntity login(LoginDto loginDto);

    //Member login(LoginDto loginDto);
    void saveUser(UserDto userDto);
}
