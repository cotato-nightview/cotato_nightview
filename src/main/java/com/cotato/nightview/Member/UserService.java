package com.cotato.nightview.Member;

import org.springframework.http.ResponseEntity;

public interface UserService {
    void saveUser(UserDto userDto);
}
