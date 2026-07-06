/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.UUID;
import sweetie.leonware.api.auth.HttpUtils;
import sweetie.leonware.api.auth.UUIDUtils;

public record ProfileRepository() {
    public UUID uuidByName(String username) {
        try {
            HttpUtils.HttpResponse response = HttpUtils.get("https://api.minecraftservices.com/users/profiles/minecraft/" + username, Map.of());
            if (response.code() != 200) {
                throw new RuntimeException("Fail " + username);
            }
            ApiProfileResponse profileResponse = JsonUtils.fromJson(response.text(), ApiProfileResponse.class);
            return UUIDUtils.parseUuid(profileResponse.id);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static class ApiProfileResponse {
        public String id;
        public String name;
    }

    public static class JsonUtils {
        private static final Gson GSON = new GsonBuilder().create();

        public static <T> T fromJson(String json, Class<T> classOfT) {
            return GSON.fromJson(json, classOfT);
        }
    }
}

