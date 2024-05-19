package datapacks;

import java.io.Serial;
import java.io.Serializable;

public record ResponsePackage(boolean errorsOccurred, String message) implements Serializable {
    @Serial
    private static final long serialVersionUID = 2659870053364412988L;
}
