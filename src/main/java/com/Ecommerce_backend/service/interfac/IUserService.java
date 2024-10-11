package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.LoginRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserById(String userId);

    Response getMyInfo(String username);

    Response deleteUser(String userId);

    Response editUser(String userId, User user);

    Response confirmEmail(String confirmationToken);

    Response changePassword(String confirmationToken, String password);

    Response forgetPassword(String email);
}
