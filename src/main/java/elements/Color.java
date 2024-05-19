package elements;

import java.util.Comparator;

/**
 * Enum describing operators hair color
 */
public enum Color implements Comparator<Color> {
    GREEN(0),
    ORANGE(1),
    WHITE(2);
    private Color(int n) {
        this.n = n;
    }

    private final int n;

    @Override
    public int compare(Color color, Color t1) {
        return color.n - t1.n;
    }
}
