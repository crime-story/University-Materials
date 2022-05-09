package model;

public class Registration {

    private Person person;
    private Course course;

    public Registration(Person person, Course course) {
        this.person = person;
        this.course = course;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "person=" + person +
                ", course=" + course +
                '}';
    }
}
