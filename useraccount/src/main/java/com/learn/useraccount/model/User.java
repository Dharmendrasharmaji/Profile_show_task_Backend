package com.learn.useraccount.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    private String id;
    private String name;
    private String age;
    private String phone;
    private String email;
    private String password;

}