package org.dean.borrower.service;

import org.dean.borrower.dto.UserRequest;
import org.dean.borrower.dto.UserResponse;
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

    public List<UserResponse> getAllUsers() {

        return userRepository.
                findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                user.getEmail()

        );
    }

    //post
    //create
    public UserResponse createUser(UserRequest request) {
        User user = new User();

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    //FindById
    public UserResponse getUserById(Long id) {


        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return null;
        }

        return toResponse(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        existingUser.setFirstname(request.getFirstname());
        existingUser.setLastname(request.getLastname());
        existingUser.setRole(request.getRole());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());

        User savedUser = userRepository.save(existingUser);

        return toResponse(savedUser);

    }

    //delete
    public boolean deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
