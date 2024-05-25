package datapacks;

import commands.Command;
import commands.Flags;

import java.io.Serial;
import java.io.Serializable;

public record RequestPackage<T>(
        String username,
        String password,
        Command command,
        String applyFor,
        T args
) implements DataPack, Serializable {
    @Serial
    private static final long serialVersionUID = 976279161664602820L;
}
