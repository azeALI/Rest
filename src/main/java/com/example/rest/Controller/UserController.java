package com.example.rest.Controller;

import com.example.rest.Model.User;
import com.example.rest.Service.MailAuth;
import com.example.rest.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;
    MailAuth mailAuth;

    public UserController(UserService userService,MailAuth mailAuth) {
        this.userService = userService;
        this.mailAuth = mailAuth;
    }

    @GetMapping("/id/{id}")
    public User findById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/role/{role}")
    public List<User> findByRole(@PathVariable String role) {
        return userService.getAllUsersByRole(role);
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @PostMapping("/verify/{email},{code}")
    public String verify(@PathVariable String email, @PathVariable String code) {
        return mailAuth.verify(email, code);
    }

    @PostMapping("/add")
    public String create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update")
    public String update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        return userService.deleteUser(id);
    }

}
