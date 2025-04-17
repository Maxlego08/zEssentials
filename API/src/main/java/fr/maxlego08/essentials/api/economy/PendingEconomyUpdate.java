package fr.maxlego08.essentials.api.economy;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public record PendingEconomyUpdate(UUID uniqueId, Economy economy, AtomicReference<BigDecimal> latestValue) {
}