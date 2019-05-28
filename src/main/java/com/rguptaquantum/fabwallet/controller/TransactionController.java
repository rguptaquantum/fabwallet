package com.rguptaquantum.fabwallet.controller;


import com.rguptaquantum.fabwallet.dto.TransactionDTO;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import com.rguptaquantum.fabwallet.model.Transaction;
import com.rguptaquantum.fabwallet.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/addMoney",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addMoney(Authentication authentication, @RequestBody TransactionDTO transactionDTO) throws WalletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        logger.debug("User with username : "+userName+" is adding money in...");
        return transactionService.addMoney(userName,transactionDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TransactionDTO> getTransactions(Authentication authentication) throws WalletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        logger.debug("User with username : "+userName+" is fetching transactions in...");
        return transactionService.getTransactions(userName);
    }
}
