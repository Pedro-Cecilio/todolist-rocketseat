package br.com.rocketseat.todolist.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rocketseat.todolist.dtos.TaskRecordDto;
import br.com.rocketseat.todolist.models.TaskModel;
import br.com.rocketseat.todolist.models.UserModel;
import br.com.rocketseat.todolist.repositories.ITaksRepository;
import br.com.rocketseat.todolist.repositories.IUserRepository;
import br.com.rocketseat.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaksRepository iTaksRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskRecordDto taskRecordDto, HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute("user");
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskRecordDto, taskModel);
        taskModel.setUserModel(user);
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio deve ser maior que a data atual.");
        }
        if (taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio deve ser maior que a data final.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.iTaksRepository.save(taskModel));
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getTasks(HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute("user");
        var tasks = this.iTaksRepository.findAllByUserModel(user);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TaskModel>> getOndeTask(HttpServletRequest request,
            @PathVariable UUID id) {
        UserModel user = (UserModel) request.getAttribute("user");
        var task = this.iTaksRepository.findByIdAndUserModel(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(HttpServletRequest request, @PathVariable UUID id,
            @RequestBody TaskRecordDto taskRecordDto) {

        UserModel user = (UserModel) request.getAttribute("user");
        var task = this.iTaksRepository.findByIdAndUserModel(id, user);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        Utils.copyNonNullProperties(taskRecordDto, task.get());
        return ResponseEntity.status(HttpStatus.OK).body(this.iTaksRepository.save(task.get()));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(HttpServletRequest request, @PathVariable UUID id) {
        UserModel user = (UserModel) request.getAttribute("user");
        var task = this.iTaksRepository.findByIdAndUserModel(id, user);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        this.iTaksRepository.delete(task.get());
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");

    }

}
