package net.fortuneblack05.typerando;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TypePickerManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Integer>>() {}.getType();

    // uuid string -> typeId
    private final Map<String, Integer> roles = new HashMap<>();
    private final Random random = new Random();

    private Path saveFile(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("typerando_roles.json");
    }

    public void load(MinecraftServer server) {
        roles.clear();
        Path file = saveFile(server);
        if (!Files.exists(file)) return;

        try (Reader r = Files.newBufferedReader(file)) {
            Map<String, Integer> loaded = GSON.fromJson(r, MAP_TYPE);
            if (loaded != null) roles.putAll(loaded);
        } catch (IOException e) {
            System.err.println("[TypeRando] Failed to load roles: " + e.getMessage());
        }
    }

    public void save(MinecraftServer server) {
        Path file = saveFile(server);
        try (Writer w = Files.newBufferedWriter(file)) {
            GSON.toJson(roles, MAP_TYPE, w);
        } catch (IOException e) {
            System.err.println("[TypeRando] Failed to save roles: " + e.getMessage());
        }
    }

    public boolean hasRole(UUID uuid) {
        return roles.containsKey(uuid.toString());
    }

    public Optional<Types> getRole(UUID uuid) {
        Integer id = roles.get(uuid.toString());
        if (id == null) return Optional.empty();
        return Types.fromId(id);
    }

    /**
     * Assign a unique unused type (1..18) if the player doesn't have one.
     * Returns empty if all 18 are taken.
     */
    public Optional<Types> assignIfMissing(UUID uuid) {
        if (hasRole(uuid)) return getRole(uuid);

        Set<Integer> used = new HashSet<>(roles.values());
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            if (!used.contains(i)) available.add(i);
        }
        if (available.isEmpty()) return Optional.empty();

        int chosen = available.get(random.nextInt(available.size()));
        roles.put(uuid.toString(), chosen);
        return Types.fromId(chosen);
    }
}