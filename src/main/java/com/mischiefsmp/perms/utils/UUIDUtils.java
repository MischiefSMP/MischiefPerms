package com.mischiefsmp.perms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class UUIDUtils {
    private static final String UUID_API = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String USERNAME_API = "https://api.mojang.com/user/profiles/%s/names";

    private static final HashMap<String, UUID> uuidCache = new HashMap<>();
    private static final HashMap<UUID, String> namesCache = new HashMap<>();

    public static UUID fixUUID(String uuid) {
        if(uuid == null)
            return null;

        uuid = uuid.replace("-", "");
        return new UUID(
                new BigInteger(uuid.substring(0, 16), 16).longValue(),
                new BigInteger(uuid.substring(16), 16).longValue());
    }

    public static ArrayList<UsernameInfo> getUsernameInfo(UUID uuid) {
        try {
            URI url = new URI(String.format(USERNAME_API, uuid));
            HttpRequest request = HttpRequest.newBuilder(url).header("accept", "application/json").build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                namesCache.put(uuid, null);
                return null;
            }

            ArrayList<UsernameInfo> info = new ArrayList<>();
            JSONArray jsonData = new JSONArray(response.body());
            for(int i = 0; i < jsonData.length(); i++) {
                JSONObject data = jsonData.getJSONObject(i);
                long changedAt = data.has("changedToAt") ? data.getLong("changedToAt") : -1;
                info.add(new UsernameInfo(data.getString("name"), changedAt));
            }
            return info;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UUID getUserUUID(String username) {
        if(uuidCache.containsKey(username))
            return uuidCache.get(username);

        Player p = Bukkit.getPlayerExact(username);
        if(p != null) {
            uuidCache.put(username, p.getUniqueId());
            namesCache.put(p.getUniqueId(), username);
            return p.getUniqueId();
        }

        try {
            URI url = new URI(String.format(UUID_API, username));
            HttpRequest request = HttpRequest.newBuilder(url).header("accept", "application/json").build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                uuidCache.put(username, null);
                return null;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            UUID uuid = fixUUID(jsonResponse.getString("id"));
            uuidCache.put(username, uuid);
            namesCache.put(uuid, username);
            return uuid;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
