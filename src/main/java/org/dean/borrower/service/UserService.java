package org.dean.borrower.service;

import org.dean.borrower.entity.User;
import org.dean.borrower.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //post
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User newUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if(existingUser == null) {
            return null;

        }

        existingUser.setFirstname(newUser.getFirstname());
        existingUser.setLastname(newUser.getLastname());
        existingUser.setRole(newUser.getRole());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPassword(newUser.getPassword());

        return userRepository.save(newUser);
        
    }
}
