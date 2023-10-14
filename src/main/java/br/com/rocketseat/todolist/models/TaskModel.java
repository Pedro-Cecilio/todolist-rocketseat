package br.com.rocketseat.todolist.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name="tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter(AccessLevel.NONE)
    private UserModel userModel;

    public UUID getUserId() {
        if (userModel != null) {
            return userModel.getId();
        } else {
            return null; // ou outro valor padrão, se apropriado
        }
    }  

    private String description;

    @Column(length=50, unique=true)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public void setTitle(String title) throws IllegalArgumentException {
        if(title.length() > 50){
            System.out.println("Entrou no erro");
            throw new IllegalArgumentException("title must be at least 50 characters");
        }
        
        this.title = title;
    }
    

    


}
