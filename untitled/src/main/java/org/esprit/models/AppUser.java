package org.esprit.models;

import java.time.LocalDate;

public class AppUser {
    private int id;
    private String role;
    private String userName;
    private String userPassword;
    private String email;
    private LocalDate creationDate;

    // Default constructor
    public AppUser() {
    }

    // Constructor without id (for new records before saving to database)
    public AppUser(String role, String userName, String userPassword, String email, LocalDate creationDate) {
        this.role = role;
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.creationDate = creationDate;
    }

    // Full constructor
    public AppUser(int id, String role, String userName, String userPassword, String email, LocalDate creationDate) {
        this.id = id;
        this.role = role;
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.creationDate = creationDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", email='" + email + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
