package com.capstone.backend.service;

import com.capstone.backend.model.dto.authentication.AuthenticationDTORequest;
import com.capstone.backend.model.dto.authentication.AuthenticationDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTORequest;
import com.capstone.backend.model.dto.register.RegisterDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTOUpdate;
import com.capstone.backend.model.dto.user.UserEmailDTORequest;
import com.capstone.backend.model.dto.user.UserForgotPasswordDTORequest;

public interface AuthenticationService {
    public RegisterDTOResponse register(RegisterDTORequest request);

    public AuthenticationDTOResponse login(AuthenticationDTORequest request) throws Exception;

    public String updateRegister(RegisterDTOUpdate request);

    public String forgotPassword(UserEmailDTORequest request);

    public Boolean changePassword(UserForgotPasswordDTORequest request);
}
