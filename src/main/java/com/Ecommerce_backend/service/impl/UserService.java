package com.Ecommerce_backend.service.impl;

import com.Ecommerce_backend.dto.LoginRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.dto.UserDTO;
import com.Ecommerce_backend.entity.ConfirmationToken;
import com.Ecommerce_backend.entity.User;
import com.Ecommerce_backend.exception.OurException;
import com.Ecommerce_backend.repo.ConfirmationTokenRepository;
import com.Ecommerce_backend.repo.UserRepository;
import com.Ecommerce_backend.service.EmailService;
import com.Ecommerce_backend.service.interfac.IUserService;
import com.Ecommerce_backend.utils.JWTUtils;
import com.Ecommerce_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + "Already Exists");
            }

            if(userRepository.existsByUsername(user.getUsername())) {
                throw new OurException(user.getUsername() + "Already Exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);
            sendConfirmationEmail(user.getEmail(), "Complete Registration!","To confirm your account, please click here: "
                    +"http://localhost:3000/confirm-account/"+confirmationToken.getConfirmationToken() );

            response.setStatusCode(200);
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During user Registration " + e.getMessage());
        }
        return response;
    }

    private void sendConfirmationEmail (String destination, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(destination);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        emailService.sendEmail(mailMessage);

    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new OurException("user not found"));


            if (!user.isEnabled()) {
                throw new OurException("User is not enabled please verify your email");
            }
            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During user Registration " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDtoList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDtoList);
        }  catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During getting all users " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setMessage("successful");
            response.setUser(userDTO);
            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During geting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String username) {
        Response response = new Response();
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setMessage("successful");
            response.setUser(userDTO);
            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During geting user info " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Deleting user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response editUser(String userId, User user) {
        Response response = new Response();

        try {

            User userData = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not found"));
            if(user.getEmail() != null ) {
                if(userRepository.existsByEmail(user.getEmail())) throw new OurException(user.getEmail() + "Already Exists");
                userData.setEmail(user.getEmail());
            }
            if(user.getUsername() != null ) {
                if(userRepository.existsByEmail(user.getUsername())) throw new OurException(user.getUsername() + "Already Exists");
                userData.setUsername(user.getUsername());
            }
            if(user.getPhoneNumber() != null) userData.setPhoneNumber(user.getPhoneNumber());
            if(user.getPassword() != null)  userData.setPassword(passwordEncoder.encode(user.getPassword()));
            if(user.getName() != null) userData.setName(user.getName());

            User updatedUser = userRepository.save(userData);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(updatedUser);


            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During user update " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response confirmEmail(String confirmationToken) {
        Response response = new Response();
        try {
            ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
            if (token == null) {
                response.setStatusCode(404);
                response.setMessage("Invalid or expired token.");
                return response;
            }


            var user = userRepository.findByEmail(token.getUserEntity().getEmail())
                    .orElseThrow(() -> new OurException("User not found"));
            user.setEnabled(true);
            userRepository.save(user);

            confirmationTokenRepository.delete(token);
            response.setStatusCode(200);
            response.setMessage("Email verified successfully!");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("User not found: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: Couldn't verify email. " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response changePassword(String confirmationToken, String password) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        Response response = new Response();
        try {
            if(token !=null) {
                var user = userRepository.findByEmail(token.getUserEntity().getEmail()).orElseThrow(() -> new OurException("user Not found"));
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                confirmationTokenRepository.delete(token);
                response.setStatusCode(200);
                response.setMessage("Password changed successfully!");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: Couldn't change password"+ e.getMessage());
        }
        return response;
    }

    @Override
    public Response forgetPassword(String email) {
        Response response = new Response();
        try {

            var user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("user Not found"));;
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            sendConfirmationEmail(user.getEmail(), "Forget your password? ","To confirm your account, please click here: "
                    +"http://localhost:3000/confirm-account/"+confirmationToken.getConfirmationToken() );

            response.setStatusCode(200);
            response.setMessage("confirm your request and change your password");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: sending confirmation email "+ e.getMessage());
        }
        return response;
    }
}
