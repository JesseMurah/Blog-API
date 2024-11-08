package com.example.blogapi.controller;

import com.example.blogapi.model.Post;
import com.example.blogapi.model.User;
import com.example.blogapi.service.PostService;
import com.example.blogapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserAndPostController {
    private static final Logger logger = LoggerFactory.getLogger(UserAndPostController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    //Endpoint to create a new user
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        logger.info("Name {}", user.getName());
        logger.info("Email {}", user.getEmail());
        logger.info("Posts {}", user.getPosts());
        logger.info("Created At {}", user.getCreatedAt());
        logger.info("Updated At {}", user.getUpdatedAt());
        return ResponseEntity.ok(createdUser);
    }

    //Endpoint to retrieve all users
    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> getUsers = userService.getAllUsers();
        return ResponseEntity.ok(getUsers);
    }

    //Endpoint to retrieve a user by their id
    @GetMapping("/get-user/{id}")
    public ResponseEntity<User> getUserId(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Endpoint to update a user by id
    @PutMapping("/update-user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> updatedUserOpt = userService.updateUser(id, userDetails);
        return updatedUserOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Endpoint to delete a user by id
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    //{The code below now does the REST Controller for the Post Service}

    //Endpoint to create a Post.
    @PostMapping("/create-post")
    public ResponseEntity<Post> createPost(@RequestBody Post post) { //
        Optional<Post> createdPostOpt = postService.createPost(post); // Calls the service to create the post
        return createdPostOpt.map(ResponseEntity::ok) // If post is created, return 200 OK with post
                .orElse(ResponseEntity.badRequest().build()); // If author not found, return 400 Bad Request
    }

    //Endpoint to retrieve all Posts.
    @GetMapping("/get-posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    //Endpoint to retrieve a Post by its ID.
    @GetMapping("/get-post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) { // @PathVariable extracts 'id' from URL
        Optional<Post> postOpt = postService.getPostById(id); // Calls the service to get the post by ID
        return postOpt.map(ResponseEntity::ok) // If post is present, return 200 OK with post
                .orElse(ResponseEntity.notFound().build()); // If not, return 404 Not Found
    }

    //Endpoint to update an existing Post.
    @PutMapping("/update-post/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Optional<Post> updatedPostOpt = postService.updatePost(id, postDetails); // Calls the service to update the post
        // Checks if the post was successfully updated
        // Returns 200 OK with the updated post
        // Could not update post, possibly due to invalid author
        // Returns 400 Bad Request
        return updatedPostOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    //Endpoint to delete a Post.
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id); // Calls the service to delete the post
        return deleted ? ResponseEntity.noContent().build() // If deleted, return 204 No Content
                : ResponseEntity.notFound().build(); // If not found, return 404 Not Found
    }
}

