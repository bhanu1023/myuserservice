package com.example.userservice.Controllers;

import com.example.userservice.Exceptions.NotFoundException;
import com.example.userservice.Exceptions.UserAlreadyExists;
import com.example.userservice.Services.UserService;
import com.example.userservice.dtos.SessionDTO;
import com.example.userservice.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDTO userDTO) throws UserAlreadyExists {
        userService.register(userDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionDTO> login(@RequestBody UserDTO userDTO) throws UserAlreadyExists, NotFoundException, NoSuchAlgorithmException {
        SessionDTO sessionDTO = userService.login(userDTO);

        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody SessionDTO sessionDTO) throws UserAlreadyExists, NotFoundException {
        userService.logout(sessionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
