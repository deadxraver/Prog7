package datapacks;

import commands.client.ClientCommand;
import java.io.Serial;
import java.io.Serializable;

public record RequestPackage(
        String username,
        String password,
        ClientCommand command,
        Object args
) implements DataPack, Serializable {
    @Serial
    private static final long serialVersionUID = 976279161664602820L;
}
