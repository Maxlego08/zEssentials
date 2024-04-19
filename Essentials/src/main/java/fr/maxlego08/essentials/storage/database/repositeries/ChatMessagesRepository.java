package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.List;
import java.util.UUID;

public class ChatMessagesRepository extends Repository {
    public ChatMessagesRepository(SqlConnection connection) {
        super(connection, "chat_message");
    }

    public void insert(ChatMessageDTO chatMessage) {
        insert(table -> {
            table.uuid("unique_id", chatMessage.unique_id());
            table.string("content", chatMessage.content());
        });
    }

    public List<ChatMessageDTO> getMessages(UUID uuid) {
        return this.select(ChatMessageDTO.class, table -> {
            table.uuid("unique_id", uuid);
            table.orderByDesc("created_at");
        });
    }
}
