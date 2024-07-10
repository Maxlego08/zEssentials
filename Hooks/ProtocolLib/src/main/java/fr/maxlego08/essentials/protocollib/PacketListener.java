package fr.maxlego08.essentials.protocollib;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.packet.PacketRegister;

public class PacketListener {

    public void registerPackets(EssentialsPlugin plugin) {

        this.register(new PacketChatListener(plugin, plugin.getModuleManager().getModuleConfiguration("chat").getString("command-placeholder.result")));
    }

    private void register(PacketRegister packetRegister) {
        packetRegister.addPacketListener();
    }

}
