package com.example.blogapi.service;


import com.example.blogapi.model.User;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    //Create a new User
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    //Retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Retrieve a user(s) by their id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //Update an existing user
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(
                user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    //update other fields if needed
                    return userRepository.save(user);
                });
    }

    //Delete a user by their ID
    public boolean deleteUser(Long id){
        return userRepository.findById(id).map(
                user -> {
                    userRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }
}
