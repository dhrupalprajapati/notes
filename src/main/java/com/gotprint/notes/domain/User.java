package com.gotprint.notes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email(message = "Invalid email id provided.")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must contains at least 8 characters.")
    @ToString.Exclude
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;

    @UpdateTimestamp
    private Timestamp updatedTime;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    List<Note> notes = new ArrayList<>();
}
