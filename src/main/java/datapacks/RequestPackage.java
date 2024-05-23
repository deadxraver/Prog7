package datapacks;

import commands.Command;

import java.io.Serial;
import java.io.Serializable;

public record RequestPackage<T>(
        String username,
        String password,
        Command command,
        T args
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 976279161664602820L;
}
