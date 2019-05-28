package com.rguptaquantum.fabwallet.validator;

import com.rguptaquantum.fabwallet.exception.WalletException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface Validator<K,V> {
    public void isTrue(@NotNull Boolean condition, @NotNull String errorMessage, int errorCode) throws WalletException;


}
