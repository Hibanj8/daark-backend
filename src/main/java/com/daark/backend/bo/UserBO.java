package com.daark.backend.bo;

import com.daark.backend.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBO {

    private Long id;
    private String username;
    private String email;
    private String telephone;
    private String role;

    public UserBO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
        this.role = user.getRole();
    }
}
