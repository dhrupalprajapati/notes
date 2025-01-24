package com.gotprint.notes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

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
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;

    @UpdateTimestamp
    private Timestamp updatedTime;
}
