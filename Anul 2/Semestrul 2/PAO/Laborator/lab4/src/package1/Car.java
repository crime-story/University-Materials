package package1;

import java.util.Objects;

public class Car {

    private String id;
    private String producer;
    private int year;

    public Car(String id, String producer, int year) {
        this.id = id;
        this.producer = producer;
        this.year = year;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return year == car.year && Objects.equals(id, car.id) && Objects.equals(producer, car.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, producer, year);
    }
}
