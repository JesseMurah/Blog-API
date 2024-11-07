package com.example.blogapi.service;


import com.example.blogapi.model.Post;
import com.example.blogapi.model.User;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    //Create a new post
    public Optional<Post> createPost(Post post) {
        Long authorId = post.getAuthor().getId();
        Optional<User> userOptional = userRepository.findById(authorId);
        if (userOptional.isPresent()) {
            post.setAuthor(userOptional.get());
            return Optional.of(postRepository.save(post));
        } else {
            //Author(User) not found
            return Optional.empty();
        }
    }

    //Retrieve all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    //Retrieve post by Id
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    //Update an existing post
    public Optional<Post> updatePost(Long id, Post postDetails) {
        User getAuthor = postDetails.getAuthor();
        Long getAuthorId = postDetails.getAuthor().getId();

        return postRepository.findById(id).flatMap(existingPost -> {
            existingPost.setTitle(postDetails.getTitle());
            existingPost.setContent(postDetails.getContent());

            //Update author if present
            if (getAuthorId != null) {
                Optional<User> newAuthorOpt = userRepository.findById(getAuthorId);
                if (newAuthorOpt.isPresent()) {
                    existingPost.setAuthor(newAuthorOpt.get());
                } else {
                    //New author not found
                    return Optional.empty();
                }
            }
            return Optional.of(postRepository.save(existingPost));
        });
    }


    //Delete post by its id
    public boolean deletePost(Long id) {
        return postRepository.findById(id).map(post -> {
            postRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}
