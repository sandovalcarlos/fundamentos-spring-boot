package com.sandoval.fundamentos.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sandoval.fundamentos.entity.User;
import com.sandoval.fundamentos.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private final Log LOG = LogFactory.getLog(UserService.class);
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Con la anotacion transactional si ocurre un error en el insert a la base de datos, este hace un rollback y se cancela toda la operacion
    //@Transactional
    public void saveTransactional(List<User> users) {
        users.stream()
                .peek(user -> LOG.info("User insert: " + user))
                //.forEach(userRepository::save)
                .forEach(user -> userRepository.save(user));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // @ManyToOne
    // @JsonBackReference
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public void delete(Long id) {
        userRepository.delete(new User(id));
    }

    public User update(User newUser, Long id) {
        return
                userRepository.findById(id)
                .map(
                        user -> {
                            user.setEmail(newUser.getEmail());
                            user.setBirthDate(newUser.getBirthDate());
                            user.setName(newUser.getName());
                            return userRepository.save(user);
                        }
                ).get();
    }
}
