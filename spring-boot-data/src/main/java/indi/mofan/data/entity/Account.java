package indi.mofan.data.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mofan
 * @date 2026/11/16 13:45
 */
public record Account(long id, String accountName, BigDecimal balance, LocalDateTime updatedAt) {
}
