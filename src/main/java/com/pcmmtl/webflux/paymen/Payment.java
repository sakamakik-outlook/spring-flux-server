package com.pcmmtl.webflux.paymen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String id;
    private String currency;
    private int amount;
    private String bic;
    private String name;
    private String direction;
    private LocalDateTime timestamp;
}
