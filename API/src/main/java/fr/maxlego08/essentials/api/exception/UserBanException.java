package fr.maxlego08.essentials.api.exception;

import fr.maxlego08.essentials.api.dto.SanctionDTO;

public class UserBanException extends Exception {

    private final SanctionDTO sanctionDTO;

    public UserBanException(SanctionDTO sanctionDTO) {
        this.sanctionDTO = sanctionDTO;
    }

    public SanctionDTO getSanctionDTO() {
        return sanctionDTO;
    }
}
