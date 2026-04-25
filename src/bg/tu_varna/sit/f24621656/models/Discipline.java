package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Discipline {
    private final String name;
    private final DisciplineType type;
    private int credits;
    private final List<Integer> availableCourses;

    public Discipline(String name, DisciplineType type) {
        this.name = name;
        this.type = type;
        this.credits = 0;
        this.availableCourses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public DisciplineType getType() {
        return type;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if(type == DisciplineType.ELECTIVE) {
            this.credits = credits;
        }
    }

    public List<Integer> getAvailableCourses() {
        return availableCourses;
    }

    public void addAvailableCourse(int course) {
        if (!availableCourses.contains(course)) {
            availableCourses.add(course);
        }
    }

    public boolean isAvailableForCourse(int course) {
        return availableCourses.contains(course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Discipline that = (Discipline) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
