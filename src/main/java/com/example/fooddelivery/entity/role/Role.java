package com.example.fooddelivery.entity.role;

import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Roles")
public class Role extends IdEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", targetEntity = User.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> users;

    public Role() {
        this.users = new HashSet<>();
    }

    public Role(String name) {
        this.name = name;
        this.users = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }

    public void addUser(User user) {
        this.users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.setRole(null);
    }
}
