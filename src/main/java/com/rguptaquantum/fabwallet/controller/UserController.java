package com.rguptaquantum.fabwallet.controller;


import com.rguptaquantum.fabwallet.dto.WalletDTO;
import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.ErrorMessage;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import com.rguptaquantum.fabwallet.service.UserService;
import com.rguptaquantum.fabwallet.service.WalletService;
import com.rguptaquantum.fabwallet.validator.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

import static com.rguptaquantum.fabwallet.exception.ErrorMessage.INVALID_AUTH_HEADER;
import static com.rguptaquantum.fabwallet.exception.ErrorMessage.MISSING_PARAMETER_IN_HEADER;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private Validator validator;

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
    @ResponseBody
    public String signup(@RequestBody UserDTO user) throws WalletException {
        user.validate();
        userService.signup(user);
        return "User Created Successfully";
    }

    @GetMapping(value = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WalletDTO getWallet(Authentication authentication) throws WalletException {
        String error;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();

        User user = userService.findUser(userName);
        error = String.format(ErrorMessage.NO_USER_FOUND, userName);
        validator.isTrue(user!=null,userName, ErrorCode.BadRequest.getCode());

        Wallet wallet = walletService.findWallet(user);
        error = String.format(ErrorMessage.NO_WALLET_FOUND, userName);
        validator.isTrue(wallet!=null,error, ErrorCode.BadRequest.getCode());

        return new WalletDTO(wallet);
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
                throw new WalletException(MISSING_PARAMETER_IN_HEADER, HttpStatus.BAD_REQUEST.value());
            }
        } else {
            throw new WalletException(INVALID_AUTH_HEADER, HttpStatus.BAD_REQUEST.value());
        }

    }
}
