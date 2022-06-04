package com.mischiefsmp.perms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.UUID;

public class UUIDUtils {
    private static final String UUID_API = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final HashMap<String, UUID> uuidCache = new HashMap<>();

    public static UUID fixUUID(String uuid) {
        if(uuid == null)
            return null;

        uuid = uuid.replace("-", "");
        return new UUID(
                new BigInteger(uuid.substring(0, 16), 16).longValue(),
                new BigInteger(uuid.substring(16), 16).longValue());
    }

    public static UUID getUserUUID(String username) {
        Player p = Bukkit.getPlayerExact(username);
        if(p != null)
            return p.getUniqueId();

        if(uuidCache.containsKey(username))
            return uuidCache.get(username);

        try {
            URI url = new URI(String.format(UUID_API, username));
            HttpRequest request = HttpRequest.newBuilder(url).header("accept", "application/json").build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            UUID uuid = fixUUID(jsonResponse.getString("id"));
            uuidCache.put(username, uuid);
            return uuid;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
