package com.project.movieshark.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.movieshark.domain.User;
import com.project.movieshark.repository.UserRepository;
import com.project.movieshark.resource.UserResource;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserResource addUser(UserResource userResource) {

        if (userRepository.existsByMobile(userResource.getMobile())) {
            return userResource;
        }

		User user = User.toEntity(userResource);

        user = userRepository.save(user);

        log.info("Added New User"+user.toString());

        return User.toResource(user);
    }


    public UserResource getUser(long id) {
        Optional<User> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            log.error("User not found for id: " + id);
            throw new EntityNotFoundException("User Not Found with ID: " + id);

        }

        return User.toResource(userEntity.get());
    }

}