package com.capstone.backend.service.impl;

import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.profle.ProfileDTOResponse;
import com.capstone.backend.model.dto.profle.ProfileDTOUpdate;
import com.capstone.backend.model.dto.user.UserChangePasswordDTORequest;
import com.capstone.backend.model.mapper.UserMapper;
import com.capstone.backend.repository.ClassRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.FileService;
import com.capstone.backend.service.UserService;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserHelper userHelper;
    ClassRepository classRepository;
    UserRepository userRepository;
    FileService fileService;
    PasswordEncoder passwordEncoder;
    MessageException messageException;

    @Override
    public ProfileDTOResponse viewProfile() {
        User userLoggedIn = userHelper.getUserLogin();
        return UserMapper.toProfileDTOResponse(userLoggedIn);
    }

    @Override
    public ProfileDTOResponse updateProfile(ProfileDTOUpdate profileDTOUpdate) {
        User userLoggedIn = userHelper.getUserLogin();
        userLoggedIn.setVillage(profileDTOUpdate.getVillage());
        userLoggedIn.setGender(profileDTOUpdate.getGender());
        userLoggedIn.setSchool(profileDTOUpdate.getSchool());
        userLoggedIn.setProvince(profileDTOUpdate.getProvince());
        userLoggedIn.setDistrict(profileDTOUpdate.getDistrict());
        userLoggedIn.setClassObject(classRepository
                .findById(profileDTOUpdate.getClassId()).orElseThrow());
        userLoggedIn.setPhone(profileDTOUpdate.getPhone());
        userLoggedIn.setLastname(profileDTOUpdate.getLastname());
        userLoggedIn.setFirstname(profileDTOUpdate.getFirstname());
        userLoggedIn.setDateOfBirth(profileDTOUpdate.getDateOfBirth());
        return UserMapper.toProfileDTOResponse(userRepository.save(userLoggedIn));
    }

    @Override
    public ProfileDTOResponse changeAvatar(MultipartFile avatar) {
        User userLoggedIn = userHelper.getUserLogin();
        String avatarUrl = fileService.setAvatar(avatar, userLoggedIn);
        userLoggedIn.setAvatar(avatarUrl);
        User user = userRepository.save(userLoggedIn);
        return UserMapper.toProfileDTOResponse(user);
    }

    @Override
    public Boolean changePassword(UserChangePasswordDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        boolean isPassword = passwordEncoder.matches(
                request.getCurrentPassword(),
                userLoggedIn.getPassword()
        );

        if (!isPassword) {
            throw ApiException.badRequestException(messageException.MSG_USER_WRONG_PASSWORD);
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw ApiException.badRequestException(messageException.MSG_USER_NOT_MATCH_PASSWORD);
        }
        userLoggedIn.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userLoggedIn = userRepository.save(userLoggedIn);
        return true;
    }
}
