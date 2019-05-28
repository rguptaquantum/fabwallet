package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.dto.TransactionDTO;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.Transaction;
import com.rguptaquantum.fabwallet.model.TransactionType;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import com.rguptaquantum.fabwallet.repository.TransactionRepository;
import com.rguptaquantum.fabwallet.repository.UserRepository;
import com.rguptaquantum.fabwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public String addMoney(String userName, TransactionDTO transactionDTO) throws WalletException {
        try {
            UUID uuid = UUID.randomUUID();
            User user = userRepository.findByUsername(userName);
            Wallet wallet = walletRepository.getByUser(user);
            Transaction transaction = new Transaction(transactionDTO);
            BigDecimal currentBalance = wallet.getBalance();
            wallet.setBalance(currentBalance.add(BigDecimal.valueOf(transaction.getAmount())));
            transaction.setGlobalId(uuid.toString());
            transaction.setWallet(wallet);
            transaction.setType(TransactionType.CREDIT);
            transactionRepository.save(transaction);
            walletRepository.save(wallet);
            return uuid.toString();
        } catch (EntityNotFoundException e) {
            throw new WalletException("Not found", HttpStatus.BAD_REQUEST.value());

        }
    }

    @Override
    public String payMoney(String userName, TransactionDTO transactionDTO) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<TransactionDTO> getTransactions(String userName) throws WalletException {
        try {
            User user = userRepository.findByUsername(userName);
            Wallet wallet = walletRepository.getByUser(user);
            return transactionRepository.getAllByWallet(wallet).stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new WalletException("Not found", HttpStatus.BAD_REQUEST.value());
        }
    }
}
