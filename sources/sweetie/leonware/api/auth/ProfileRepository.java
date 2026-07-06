package sweetie.leonware.api.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Map;
import java.util.UUID;
import sweetie.leonware.api.auth.HttpUtils;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/ProfileRepository.class */
public final class ProfileRepository extends Record {

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/ProfileRepository$ApiProfileResponse.class */
    public static class ApiProfileResponse {
        public String id;
        public String name;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ProfileRepository.class), ProfileRepository.class, "").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ProfileRepository.class), ProfileRepository.class, "").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ProfileRepository.class, Object.class), ProfileRepository.class, "").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public UUID uuidByName(String username) {
        try {
            HttpUtils.HttpResponse response = HttpUtils.get("https://api.minecraftservices.com/users/profiles/minecraft/" + username, Map.of());
            if (response.code() != 200) {
                throw new RuntimeException("Fail " + username);
            }
            ApiProfileResponse profileResponse = (ApiProfileResponse) JsonUtils.fromJson(response.text(), ApiProfileResponse.class);
            return UUIDUtils.parseUuid(profileResponse.id);
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/ProfileRepository$JsonUtils.class */
    public static class JsonUtils {
        private static final Gson GSON = new GsonBuilder().create();

        public static <T> T fromJson(String str, Class<T> cls) {
            return (T) GSON.fromJson(str, (Class) cls);
        }
    }
}
