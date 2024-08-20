package fr.maxlego08.essentials.api.vault;

/**
 * A record representing the result of a vault operation.
 * It contains the vault where the operation took place and the slot involved in the operation.
 *
 * @param vault the vault where the operation occurred
 * @param slot  the slot within the vault involved in the operation
 */
public record VaultResult(Vault vault, int slot) {
}
