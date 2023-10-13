package br.com.rocketseat.todolist.controllers;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rocketseat.todolist.dtos.UserRecordDto;
import br.com.rocketseat.todolist.models.UserModel;
import br.com.rocketseat.todolist.repositories.IUserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository iUserRepository;
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserRecordDto userRecordDto){
        var user = this.iUserRepository.findByUsername(userRecordDto.username());
        var passwordHash = BCrypt.withDefaults().hashToString(12, userRecordDto.password().toCharArray());
        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username já cadastrado.");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        userModel.setPassword(passwordHash);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.iUserRepository.save(userModel));
    }
}
