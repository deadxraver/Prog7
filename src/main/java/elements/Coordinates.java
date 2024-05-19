package elements;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class storing two numbers reflecting coordinates - x and y
 * <p>
 * x cannot be null
 * <p>
 * y cannot be null and should not be greater than 274
 */
public class Coordinates implements Serializable {
    public Coordinates(Float x, Double y) {
        this.x = x;
        this.y = y;
    }
    private Float x; //Поле не может быть null
    private Double y; //Максимальное значение поля: 274, Поле не может быть null

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
    public Double getLength() {
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
    }
    @Serial
    private static final long serialVersionUID = -2506907464839793438L;
}
