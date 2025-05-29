package com.daark.backend.mapper;

import com.daark.backend.bo.UserBO;
import com.daark.backend.dto.UserDTO;
import com.daark.backend.entity.User;

public class UserMapper {

    public static UserBO toBO(User user) {
        return new UserBO(user);
    }

    public static UserDTO toDTO(UserBO bo) {
        UserDTO dto = new UserDTO();
        dto.setId(bo.getId());
        dto.setUsername(bo.getUsername());
        dto.setEmail(bo.getEmail());
        dto.setTelephone(bo.getTelephone());
        return dto;
    }

    public static UserBO toBO(UserDTO dto) {
        UserBO bo = new UserBO();
        bo.setId(dto.getId());
        bo.setUsername(dto.getUsername());
        bo.setEmail(dto.getEmail());
        bo.setTelephone(dto.getTelephone());
        return bo;
    }
}
