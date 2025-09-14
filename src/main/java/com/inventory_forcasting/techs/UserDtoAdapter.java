package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.AppUsers;
import org.springframework.stereotype.Component;

import com.inventory_forcasting.models.UsersDTO;

@Component
public class UserDtoAdapter implements DtoEntityAdapter<UsersDTO, AppUsers>{

    @Override
    public AppUsers convertToEntity(UsersDTO dto) {
        AppUsers user = AppUsers.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
        if(dto.getId()!=null){
            user.setId(dto.getId());
        }
        return user;
    }

    @Override
    public UsersDTO convertToDto(AppUsers user) {
        UsersDTO dto = UsersDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        if(user.getId()!=null){
            dto.setId(user.getId());
        }
        return dto;
    }


}
