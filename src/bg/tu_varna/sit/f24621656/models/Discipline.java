package bg.tu_varna.sit.f24621656.models;

import bg.tu_varna.sit.f24621656.enums.DisciplineType;

import java.util.ArrayList;
import java.util.List;

public class Discipline {
    private final String name;
    private final DisciplineType type;
    private int credits;                                        // само за избираеми (за задължителни може да е 0)
    private final List<Integer> availableCourses;               // в кои курсове може да се записва

    public Discipline(String name, DisciplineType type) {
        this.name = name;
        this.type = type;
        this.credits = 0;
        this.availableCourses = new ArrayList<>();
    }

    public Discipline(String name, DisciplineType type, int credits, List<Integer> availableCourses) {
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.availableCourses = new ArrayList<>(availableCourses);
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
        availableCourses.add(course);
    }

    public boolean isAvailableForCourse(int course) {
        return availableCourses.contains(course);
    }
}
