package com.Ecommerce_backend.controller;

import com.Ecommerce_backend.dto.ForgetPasswordDTO;
import com.Ecommerce_backend.dto.LoginRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.User;
import com.Ecommerce_backend.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register (@RequestBody User user){
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> Login (@RequestBody LoginRequest loginRequest){
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/confirm-email/{confirmationToken}")
    public ResponseEntity<Response> ConfirmEmail(@PathVariable String confirmationToken) {
        Response response = userService.confirmEmail(confirmationToken);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Response> ForgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        Response response = userService.forgetPassword(forgetPasswordDTO.getEmail());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Response> ChangePassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        Response response = userService.changePassword(forgetPasswordDTO.getConfirmationToken(),forgetPasswordDTO.getPassword());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
