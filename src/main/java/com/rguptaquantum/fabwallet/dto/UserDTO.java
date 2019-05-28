package com.rguptaquantum.fabwallet.dto;

import com.rguptaquantum.fabwallet.exception.WalletException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.validator.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import static com.rguptaquantum.fabwallet.exception.ErrorMessage.INVALID_SIGNUP_DETAILS;

@Getter
@Setter
public class UserDTO {

    private String username;
    private String password;
    private String email;

    public void validate() throws WalletException {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)||
                password.length()<8||StringUtils.isEmpty(email)||!EmailValidator.getInstance().isValid(email)) {
            throw new WalletException(INVALID_SIGNUP_DETAILS, HttpStatus.BAD_REQUEST.value());
        }
    }
}
