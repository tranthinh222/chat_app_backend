package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.domain.request.ReqCreateUserDto;
import com.thinhtran.chatapp.domain.response.ResUserDto;
import com.thinhtran.chatapp.service.UserService;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
import com.thinhtran.chatapp.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService,  PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDto> getUserById(@PathVariable Long id){
        User user = this.userService.getUserById(id);
        if (user == null){
            throw new IdInvalidException("User with id " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(user));
    }

    @PostMapping("/users/")
    @ApiMessage("create an user")
    public ResponseEntity<User> createUser(@RequestBody ReqCreateUserDto user)throws Exception {
        boolean isEmailExist = this.userService.existsByEmail(user.getEmail());
        if (isEmailExist) {
            throw new Exception("User with email " + user.getEmail() + " already exists");
        }
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(user));
    }


}
