package com.rguptaquantum.fabwallet.model;

import com.rguptaquantum.fabwallet.dto.TransactionDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name="type_id")
    private TransactionType type;

    @Column(name= "global_id",unique = true, nullable = false)
    private String globalId;

    @NotNull
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wallet_id")
    private Wallet wallet;

    @Column
    String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_updated")
    private Date lastUpdated;

    @Column(name="last_updated_by")
    private String lastUpdatedBy;

    public Transaction() {}

    public Transaction(TransactionDTO transactionDTO) {
        this.amount = transactionDTO.getAmount();
        this.description = transactionDTO.getDescription();
    }
}
