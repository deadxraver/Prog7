package datapacks;

import java.io.Serial;
import java.io.Serializable;

public record AuthorizationPackage(
        boolean isRegistered,
        String username,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5056100572597227192L;
}
