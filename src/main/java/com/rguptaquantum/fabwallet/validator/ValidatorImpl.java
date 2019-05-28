package com.rguptaquantum.fabwallet.validator;

import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.WalletException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Validated
@Component
public class ValidatorImpl implements Validator<String,String> {


    @Override
    public void isTrue(@NotNull Boolean condition,@NotNull String errorMessage, int errorCode) throws WalletException{
        if(!condition){
            throw new WalletException(errorMessage, errorCode);
        }
    }
}
