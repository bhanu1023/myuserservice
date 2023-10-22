package com.example.userservice.Services;

import com.example.userservice.Exceptions.NotFoundException;
import com.example.userservice.Exceptions.UserAlreadyExists;
import com.example.userservice.Models.Sessions;
import com.example.userservice.Models.User;
import com.example.userservice.Repositories.SessionRepository;
import com.example.userservice.Repositories.UserRepository;
import com.example.userservice.Util.JwtTokenUtil;
import com.example.userservice.dtos.SessionDTO;
import com.example.userservice.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenUtil jwtTokenUtil){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public SessionDTO login(UserDTO userDTO) throws NotFoundException, NoSuchAlgorithmException {
//        Optional<User> OptionalUser = userRepository.findUserByEmailAndEncPass(userDTO.getEmail(), userDTO.getEncPass());
        Optional<User> OptionalUser = userRepository.findByEmail(userDTO.getEmail());
        if(OptionalUser.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User existingUser = OptionalUser.get();
        if(!bCryptPasswordEncoder.matches(userDTO.getEncPass(), existingUser.getEncPass())){
            throw new NotFoundException("User not found");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("email", existingUser.getEmail());

        Sessions sessions = new Sessions();
        sessions.setToken(jwtTokenUtil.generateToken(map));
        sessions.setUser(existingUser);

        sessionRepository.save(sessions);
        return convertToSessionDTO(sessions);
    }

    @Override
    public void register(UserDTO userDTO) throws UserAlreadyExists {
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(!userOptional.isEmpty()){
            throw new UserAlreadyExists("User already exists");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setEncPass(bCryptPasswordEncoder.encode(userDTO.getEncPass()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void logout(SessionDTO sessionDTO) throws NotFoundException {
        Optional<Sessions> sessionsOptional = sessionRepository.findByToken(sessionDTO.getToken());
        if(sessionsOptional.isEmpty()){
            throw new NotFoundException("Session not found");
        }
        Sessions sessions = sessionsOptional.get();
        System.out.println(sessions.getToken());
        sessionRepository.deleteByToken(sessions.getToken());
    }

    SessionDTO convertToSessionDTO(Sessions sessions){
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setToken(sessions.getToken());
        return sessionDTO;
    }
}
