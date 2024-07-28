package fr.maxlego08.essentials.api.vote;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoteCache {
    private final Map<UUID, UserVoteDTO> voteMap = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastUpdateMap = new ConcurrentHashMap<>();
    private final Map<UUID, WrappedTask> tasks = new ConcurrentHashMap<>();

    public synchronized void addVote(UUID uniqueId, boolean isOnline) {
        UserVoteDTO voteDTO = voteMap.getOrDefault(uniqueId, new UserVoteDTO(uniqueId, 0, 0));
        voteDTO = new UserVoteDTO(uniqueId, voteDTO.vote() + 1, isOnline ? 0 : voteDTO.vote_offline() + 1);
        voteMap.put(uniqueId, voteDTO);
        lastUpdateMap.put(uniqueId, System.currentTimeMillis());
    }

    public synchronized void addTask(UUID uniqueId, WrappedTask wrappedTask) {
        this.tasks.put(uniqueId, wrappedTask);
    }

    public boolean hasTask(UUID uuid) {
        return this.tasks.containsKey(uuid);
    }

    public synchronized UserVoteDTO clearVote(UUID uniqueId) {
        this.lastUpdateMap.remove(uniqueId);
        this.tasks.remove(uniqueId);
        return voteMap.remove(uniqueId);
    }

    public long getLastUpdateTimestamp(UUID uniqueId) {
        return lastUpdateMap.getOrDefault(uniqueId, 0L);
    }

}
