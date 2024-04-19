package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.ChatMessageDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

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


}
