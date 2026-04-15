package com.tgr.accountservice.domain.account;

import java.math.BigDecimal;
import java.util.UUID;

public interface Models {
    public record Account(
            Long id,
            UUID ownerId,
            BigDecimal balance,
            String currency) {

    }
}
