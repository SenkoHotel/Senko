package senkohotel.senko.util;

import java.util.HashMap;
import java.util.List;

public class AfkUtils {
    static HashMap<String, String> afkUsers = new HashMap<>();

    public static void addUser(String uid, String reason) {
        afkUsers.put(uid, reason);
    }

    public static void removeUser(String uid) {
        afkUsers.remove(uid);
    }

    public static String isAfk(String uid) {
        if (afkUsers.containsKey(uid))
            return afkUsers.get(uid);

        return "";
    }
}
