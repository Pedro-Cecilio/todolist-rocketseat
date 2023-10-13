package br.com.rocketseat.todolist.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rocketseat.todolist.models.UserModel;


@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID>{
    UserModel findByUsername(String username);
}
