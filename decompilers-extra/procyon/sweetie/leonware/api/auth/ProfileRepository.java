// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.auth;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.util.Map;
import java.util.UUID;

record ProfileRepository() {
    public UUID uuidByName(final String username) {
        try {
            final HttpUtils.HttpResponse response = HttpUtils.get("https://api.minecraftservices.com/users/profiles/minecraft/" + username, Map.of());
            if (response.code() != 200) {
                throw new RuntimeException("Fail " + username);
            }
            final ApiProfileResponse profileResponse = JsonUtils.fromJson(response.text(), ApiProfileResponse.class);
            return UUIDUtils.parseUuid(profileResponse.id);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static class ApiProfileResponse
    {
        public String id;
        public String name;
    }
    
    public static class JsonUtils
    {
        private static final Gson GSON;
        
        public static <T> T fromJson(final String json, final Class<T> classOfT) {
            return JsonUtils.GSON.fromJson(json, classOfT);
        }
        
        static {
            GSON = new GsonBuilder().create();
        }
    }
}
