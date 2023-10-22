package com.example.userservice.Services;

import com.example.userservice.Exceptions.NotFoundException;
import com.example.userservice.Exceptions.UserAlreadyExists;
import com.example.userservice.dtos.SessionDTO;
import com.example.userservice.dtos.UserDTO;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    public SessionDTO login(UserDTO userDTO) throws NotFoundException, NoSuchAlgorithmException;
    public void register(UserDTO userDTO) throws UserAlreadyExists;
    public void logout(SessionDTO sessionDTO) throws NotFoundException;
}
