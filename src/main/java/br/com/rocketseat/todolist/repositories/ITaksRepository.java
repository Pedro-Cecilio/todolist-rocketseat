package br.com.rocketseat.todolist.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rocketseat.todolist.models.TaskModel;
import br.com.rocketseat.todolist.models.UserModel;

import java.util.List;
import java.util.Optional;

public interface ITaksRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findAllByUserModel(UserModel userModel);

    Optional<TaskModel> findByIdAndUserModel(UUID id, UserModel userModel);

}

