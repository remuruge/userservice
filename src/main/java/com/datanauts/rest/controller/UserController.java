package com.datanauts.rest.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datanauts.rest.dao.UserDAO;
import com.datanauts.rest.model.User;
import com.datanauts.rest.model.Users;

@RestController
@RequestMapping(path = "/users")
public class UserController 
{
    @Autowired
    private UserDAO userDao;
    
    @GetMapping(path="/", produces = "application/json")
    public Users getUsers() 
    {
        return userDao.getAllUsers();
    }
    
    @PostMapping(path= "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addUser(
                        @RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
                        @RequestHeader(name = "X-COM-LOCATION", required = false, defaultValue = "ASIA") String headerLocation,
                        @RequestBody User user) 
                 throws Exception 
    {       
        //Generate resource id
        Integer id = userDao.getAllUsers().getUserList().size() + 1;
        user.setId(id);
        
        //add resource
        userDao.addUser(user);
        
        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand(user.getId())
                                    .toUri();
        
        //Send location in response
        return ResponseEntity.created(location).build();
    }
}
