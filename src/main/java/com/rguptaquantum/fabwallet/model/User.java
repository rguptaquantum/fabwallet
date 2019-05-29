package com.rguptaquantum.fabwallet.model;

import com.rguptaquantum.fabwallet.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user", schema = "fabwallet")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;

    public User() {}

    public User(UserDTO userDTO) {
        this.setEmail(userDTO.getEmail());
        this.setPassword(userDTO.getPassword());
        this.setUsername(userDTO.getUsername());
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_CLIENT);
        this.setRoles(roles);
    }
}
