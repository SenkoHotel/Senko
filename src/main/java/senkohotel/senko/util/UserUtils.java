package senkohotel.senko.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class UserUtils {
    public static boolean hasRole(Member m, Role r) {
        return hasRole(m, r.getId());
    }

    public static boolean hasRole(Member m, String r) {
        for (Role role : m.getRoles()) {
            if (role.getId().equals(r))
                return true;
        }
        return false;
    }
}
