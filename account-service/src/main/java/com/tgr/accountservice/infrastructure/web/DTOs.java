package com.tgr.accountservice.infrastructure.web;

import java.math.BigDecimal;

public interface DTOs {
     record Account(BigDecimal balance, String currency) { }
}
