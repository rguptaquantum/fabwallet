package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.dto.TransactionDTO;
import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.ErrorMessage;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.Transaction;
import com.rguptaquantum.fabwallet.model.TransactionType;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import com.rguptaquantum.fabwallet.repository.TransactionRepository;
import com.rguptaquantum.fabwallet.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private Validator validator;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public String addMoney(String userName, TransactionDTO transactionDTO) throws WalletException {

        String error;
        UUID uuid = UUID.randomUUID();

        User user = userService.findUser(userName);
        error = String.format(ErrorMessage.NO_USER_FOUND, userName);
        validator.isTrue(user!=null,userName, ErrorCode.BadRequest.getCode());


        Wallet wallet = walletService.findWallet(user);
        error = String.format(ErrorMessage.NO_WALLET_FOUND, userName);
        validator.isTrue(wallet!=null,error, ErrorCode.BadRequest.getCode());

        Transaction transaction = new Transaction(transactionDTO);

        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
        wallet = walletService.updateWalletAmount(wallet,amount,TransactionType.CREDIT);

        transaction.setGlobalId(uuid.toString());
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.CREDIT);
        transactionRepository.save(transaction);

        return uuid.toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public String payMoney(String userName, TransactionDTO transactionDTO) throws WalletException {
        String error;
        UUID uuid = UUID.randomUUID();

        User user = userService.findUser(userName);
        error = String.format(ErrorMessage.NO_USER_FOUND, userName);
        validator.isTrue(user!=null,userName, ErrorCode.BadRequest.getCode());


        Wallet wallet = walletService.findWallet(user);
        error = String.format(ErrorMessage.NO_WALLET_FOUND, userName);
        validator.isTrue(wallet!=null,error, ErrorCode.BadRequest.getCode());

        Transaction transaction = new Transaction(transactionDTO);

        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
        wallet = walletService.updateWalletAmount(wallet,amount,TransactionType.DEBIT);

        transaction.setGlobalId(uuid.toString());
        transaction.setWallet(wallet);
        transaction.setType(TransactionType.DEBIT);
        transactionRepository.save(transaction);

        return uuid.toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<TransactionDTO> getTransactions(String userName) throws WalletException {
        String error;

        User user = userService.findUser(userName);
        error = String.format(ErrorMessage.NO_USER_FOUND, userName);
        validator.isTrue(user!=null,userName, ErrorCode.BadRequest.getCode());


        Wallet wallet = walletService.findWallet(user);
        error = String.format(ErrorMessage.NO_WALLET_FOUND, userName);
        validator.isTrue(wallet!=null,error, ErrorCode.BadRequest.getCode());

        return transactionRepository.getAllByWallet(wallet).stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }
}
