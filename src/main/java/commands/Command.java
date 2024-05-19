package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

public interface Command {
    ResponsePackage run(MovieCollection movieCollection, Object arg);
    String explain();
}
