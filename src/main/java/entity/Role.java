package entity;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Roles")
public class Role extends IdEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", targetEntity = User.class)
    private Set<User> users;

    public Role() {
        this.users = new HashSet<>();
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public boolean removeUsers(User user) {
        return this.users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Role role = (Role) o;
        return this.getId() == role.getId();
    }

    @Override
    public int hashCode()  {
        return Long.hashCode(this.getId());
    }
}
