package com.rguptaquantum.fabwallet.exception;

public class ErrorMessage {
    public static final String NO_WALLET_FOUND = "No wallet for username %s exists in the system.";
    public static final String NO_USER_FOUND = "No user with username %s exists in the system.";
    public static final String NOT_ENOUGH_FUNDS = "Wallet has not enough funds to perform debit transaction with amount %s";
    public static final String INVALID_AMOUNT = "Amount is not valid for transaction";
    public static final String MISSING_PARAMETER_IN_HEADER = "username or password missing in auth header";
    public static final String INVALID_AUTH_HEADER = "Invalid basic auth header";
    public static final String INVALID_SIGNUP_DETAILS = "Details are invalid";
    public static final String USER_ALREADY_EXIST = "User already exist";
}
