package serialization;

import java.io.Serializable;

public class Car implements Serializable {

    private String type;
    private transient String color;
    private transient Owner owner; // must mark transient, or Owner must implement Serializable as well to serialise Car

    public Car(String type, String color, Owner owner) {
        this.type = type;
        this.color = color;
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Car{" +
                "type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", owner=" + owner +
                '}';
    }
}
