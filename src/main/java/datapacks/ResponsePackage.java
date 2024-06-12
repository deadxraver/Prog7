package datapacks;

import elements.Movie;

import java.io.Serial;
import java.io.Serializable;

public record ResponsePackage(
        boolean errorsOccurred,
        Object response,
        Movie movie
) implements DataPack, Serializable {
    @Serial
    private static final long serialVersionUID = 2659870053364412988L;
}
