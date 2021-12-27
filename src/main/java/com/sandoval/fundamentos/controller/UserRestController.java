package com.sandoval.fundamentos.controller;

import com.sandoval.fundamentos.caseuse.CreateUser;
import com.sandoval.fundamentos.caseuse.DeleteUser;
import com.sandoval.fundamentos.caseuse.GetUser;
import com.sandoval.fundamentos.caseuse.UpdateUser;
import com.sandoval.fundamentos.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    // Create, get, delete, update
    private GetUser getUser;
    private CreateUser createUser;
    private DeleteUser deleteUser;
    private UpdateUser updateUser;

    public UserRestController(GetUser getUser, CreateUser createUser, DeleteUser deleteUser, UpdateUser updateUser) {
        this.getUser = getUser;
        this.createUser = createUser;
        this.deleteUser = deleteUser;
        this.updateUser = updateUser;
    }
    @GetMapping("/")
    List<User> get() {
        return getUser.getAll();
    }

    // @PostMapping("/")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<User> newUser(@RequestBody(required = false) User newUser) {
        return new ResponseEntity<>(createUser.save(newUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteUser(@PathVariable Long id) {
        deleteUser.remove(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    ResponseEntity<User> replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return new ResponseEntity<>(updateUser.update(newUser, id), HttpStatus.OK);

    }


}
