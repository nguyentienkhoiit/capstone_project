package com.capstone.backend.service;


import com.capstone.backend.model.dto.profle.ProfileDTOResponse;
import com.capstone.backend.model.dto.profle.ProfileDTOUpdate;
import com.capstone.backend.model.dto.user.UserChangePasswordDTORequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public ProfileDTOResponse viewProfile();

    public ProfileDTOResponse updateProfile(ProfileDTOUpdate profileDTOUpdate);

    public ProfileDTOResponse changeAvatar(MultipartFile avatar);

    public Boolean changePassword(UserChangePasswordDTORequest request);
}
