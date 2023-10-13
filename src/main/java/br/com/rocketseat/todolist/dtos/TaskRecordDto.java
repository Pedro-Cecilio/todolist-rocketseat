package br.com.rocketseat.todolist.dtos;

import java.time.LocalDateTime;

public record TaskRecordDto(String description, String title, LocalDateTime startAt, LocalDateTime endAt, String priority) {
    
}
