package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.PrivateMessageDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class PrivateMessagesRepository extends Repository {
    public PrivateMessagesRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "private_messages");
    }

    /*public void insert(PrivateMessageDTO privateMessageDTO) {
        insert(table -> {
            table.uuid("sender_unique_id", privateMessageDTO.sender_unique_id());
            table.uuid("receiver_unique_id", privateMessageDTO.receiver_unique_id());
            table.string("content", privateMessageDTO.content());
        });
    }*/

    public List<PrivateMessageDTO> getMessages(UUID uuid) {
        return this.select(PrivateMessageDTO.class, table -> {
            table.uuid("sender_unique_id", uuid);
            table.orderByDesc("created_at");
        });
    }

    public void insertMessages(List<PrivateMessageDTO> privateMessages) {
        insert(privateMessages.stream().map(dto -> schema(table -> {
            table.uuid("sender_unique_id", dto.sender_unique_id());
            table.uuid("receiver_unique_id", dto.receiver_unique_id());
            table.string("content", dto.content());
        })).toList());
    }
}
