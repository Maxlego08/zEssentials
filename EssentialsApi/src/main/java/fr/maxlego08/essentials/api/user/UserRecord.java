package fr.maxlego08.essentials.api.user;

import fr.maxlego08.essentials.api.database.dto.PlayTimeDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;

import java.util.List;

public record UserRecord(UserDTO userDTO, List<PlayTimeDTO> playTimeDTOS) {
}
