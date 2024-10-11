package com.Ecommerce_backend.controller;


import com.Ecommerce_backend.exception.OurException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController {

    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public void handleNonExistentPost() {
        throw new OurException("This POST endpoint does not exist.");
    }


}