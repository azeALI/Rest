package com.example.rest.Service;

import com.example.rest.Model.User;
import com.example.rest.Repository.UserRepository;
import com.example.rest.Repository.VerificationRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final MailAuth mailAuth;

    public UserService(UserRepository userRepository, @Lazy MailAuth mailAuth, VerificationRepository verificationRepository) {
        this.userRepository = userRepository;
        this.mailAuth = mailAuth;
        this.verificationRepository = verificationRepository;
    }


    public User getUserById(long id) {
        return userRepository.findById1(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    public String createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email Already Used";
        } else if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username Already Taken";
        } else {
            if (verificationRepository.existsById(user.getEmail())) {
                if (verificationRepository.getReferenceById(user.getEmail()).isStatus()) {
                    user.setId(userRepository.lastId() + 1);
                    userRepository.save(user);
                    verificationRepository.deleteById(user.getEmail());
                    return "User Successfully created";
                }
                else{
                    return "Email Already Sent";
                }
            }
            return mailAuth.send(user);
        }
    }

    public String updateUser(User user) {
        if (userRepository.findById1(user.getId()) == null) {
            return "User Not Found";
        } else {
            if (userRepository.checkSameEmail(user.getId(), user.getEmail()) != null) {
                return "Email Already Used";
            } else if (userRepository.checkSameUsername(user.getId(), user.getUsername()) != null) {
                return "Username Already Taken";
            }
            userRepository.save(user);
            return "User updated";
        }
    }

    public String deleteUser(long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }

}
