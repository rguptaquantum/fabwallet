package com.rguptaquantum.fabwallet.controller;


import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final String BASIC_TYPE = "Basic ";

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/signin",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AuthenticationToken signin(@RequestHeader("Authorization") String authHeader) throws WalletException {
        UserDTO userDTO = getUserDTOFromHeader(authHeader);
        logger.debug("User with username : "+userDTO.getUsername()+" is signing in...");
        return userService.signin(userDTO);
    }


    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean signup(@RequestBody UserDTO user) {
        return userService.signup(user);
    }

    private UserDTO getUserDTOFromHeader(String authHeader) throws WalletException {
        String base64Encoded;
        String username;
        String password;
        UserDTO userDTO = new UserDTO();
        Base64.Decoder decoder = Base64.getDecoder();
        if (authHeader != null && authHeader.startsWith(BASIC_TYPE)) {
            base64Encoded = new String(decoder.decode(authHeader.substring(6)));
            String[] credentials = base64Encoded.split(":");
            if(credentials.length==2) {
                userDTO.setUsername(credentials[0]);
                userDTO.setPassword(credentials[1]);
                return userDTO;
            } else {
                throw new WalletException("username or password missing in auth header", HttpStatus.BAD_REQUEST.value());
            }
        } else {
            throw new WalletException("Invalid basic auth header", HttpStatus.BAD_REQUEST.value());
        }

    }
}
