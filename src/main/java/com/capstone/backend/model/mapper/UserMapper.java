package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.Role;
import com.capstone.backend.entity.User;
import com.capstone.backend.model.dto.profle.ProfileDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTORequest;
import com.capstone.backend.model.dto.register.RegisterDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTOUpdate;
import com.capstone.backend.model.dto.role.RoleDTODisplay;
import com.capstone.backend.model.dto.role.RoleDTOResponse;
import com.capstone.backend.model.dto.user.UserDTOResponse;
import com.capstone.backend.utils.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.capstone.backend.utils.Constants.HOST_SERVER;

public class UserMapper {
    public static User toUser(RegisterDTORequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .active(false)
                .avatar("default-avatar.jpg")
                .createdAt(LocalDateTime.now())
                .violationTime(0L)
                .totalPoint(Constants.TOTAL_POINT_DEFAULT)
                .build();
    }

    public static User toUser(RegisterDTOUpdate request, User user) {
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setSchool(request.getSchool());
        user.setProvince(request.getProvince());
        user.setDistrict(request.getDistrict());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setVillage(request.getVillage());
        if (request.getClassId() != null)
            user.setClassObject(Class.builder().id(request.getClassId()).build());
        return user;
    }

    public static RegisterDTOResponse toRegisterDTOResponse(User user) {
        return RegisterDTOResponse.builder()
                .id(user.getId())
                .build();
    }

    public static UserDTOResponse toUserDTOResponse(User user) {
        return UserDTOResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .avatar(HOST_SERVER + "/" + user.getAvatar())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .school(user.getSchool())
                .province(user.getProvince())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static ProfileDTOResponse toProfileDTOResponse(User user) {
        Long classId = user.getClassObject() != null ? user.getClassObject().getId() : null;
        List<RoleDTODisplay> roleDTOResponses = user.getUserRoleList().stream()
                .map(role -> RoleMapper.toRoleDTODisplay(role.getRole()))
                .toList();
        return ProfileDTOResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .avatar(HOST_SERVER + "/" + user.getAvatar())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .school(user.getSchool())
                .province(user.getProvince())
                .village(user.getVillage())
                .createdAt(user.getCreatedAt())
                .classId(classId)
                .roleDTOResponses(roleDTOResponses)
                .build();
    }
}
