package com.progressoft.clustereddata_warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "deals")
public class FxDeal {
    @Id
    private String id;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must follow the correct format")
    @Column(name = "from_currency", nullable = false, length = 3)
    @Size(min = 3, max = 3)
    @NotNull
    private String fromCurrency;


    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must follow the correct format")
    @Column(name = "to_currency", nullable = false, length = 3)
    @Size(min = 3, max = 3)
    @NotNull
    private String toCurrency;

    @Positive
    @Column(name = "deal_amount", nullable = false)
    @NotNull
    private BigDecimal dealAmount;

    @Column(name = "deal_timestamp", nullable = false)
    @NotNull
    @CreatedDate
    private LocalDateTime dealTimestamp;
}
