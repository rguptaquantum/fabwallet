package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.ErrorMessage;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.TransactionType;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import com.rguptaquantum.fabwallet.repository.WalletRepository;
import com.rguptaquantum.fabwallet.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private Validator validator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Wallet findWallet(User user) {
        return walletRepository.findByUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public Wallet updateWalletAmount(@NotNull Wallet wallet, @NotNull BigDecimal amount, TransactionType transactionType) throws WalletException {
        if(transactionType.equals(TransactionType.DEBIT)) {
            Boolean condition = wallet.getBalance().compareTo(amount) >= 0;
            validator.isTrue(condition, String.format(ErrorMessage.NOT_ENOUGH_FUNDS,amount.toString()), ErrorCode.BadRequest.getCode());
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else {
            wallet.setBalance(wallet.getBalance().add(amount));
        }
        wallet.setLastUpdated(new Date());
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public Wallet createWalletForUser(User user) {
        Wallet wallet = new Wallet(user);
        return walletRepository.save(wallet);
    }
}
