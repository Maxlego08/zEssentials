package fr.maxlego08.essentials.hooks;

import com.artillexstudios.axvaults.AxVaults;
import com.artillexstudios.axvaults.libs.axapi.serializers.Serializers;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class AxVaultsHook {


    public AxVaultsHook(EssentialsPlugin plugin) {
        loadVaults(plugin);
    }

    public void loadVaults(EssentialsPlugin plugin) {
        try {

            var manager = plugin.getVaultManager();
            var database = AxVaults.getDatabase();
            Field field = database.getClass().getDeclaredField("conn");
            field.setAccessible(true);

            Object conn = field.get(database);

            MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            MethodType typePrepareStatement = MethodType.methodType(PreparedStatement.class, String.class);
            MethodHandle prepareStatement = lookup.findVirtual(conn.getClass(), "prepareStatement", typePrepareStatement);

            try (PreparedStatement statement = (PreparedStatement) prepareStatement.invoke(conn, "SELECT * FROM axvaults_data ORDER BY uuid ASC")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {

                        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                        byte[] bytes = resultSet.getBytes("storage");
                        ItemStack[] itemStacks = Serializers.ITEM_ARRAY.deserialize(bytes);

                        for (ItemStack itemStack : itemStacks) {
                            if (itemStack != null && !itemStack.getType().isAir()) {
                                manager.addItem(uuid, itemStack);
                            }
                        }
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
