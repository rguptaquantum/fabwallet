package com.rguptaquantum.fabwallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "wallet", schema = "fabwallet")
public class Wallet {
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Min(0)
    @Column(nullable = false)
    private BigDecimal balance;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_updated")
    private Date lastUpdated;


    public Wallet() {}

    public Wallet(User user) {
        this.setBalance(BigDecimal.valueOf(0));
        this.setUser(user);
    }


}
