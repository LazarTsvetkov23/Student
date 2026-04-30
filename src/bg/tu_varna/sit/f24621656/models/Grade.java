package bg.tu_varna.sit.f24621656.models;

public class Grade {
    private final Discipline discipline;
    private final double value;

    public Grade(Discipline discipline, double value) {
        this.discipline = discipline;
        this.value = value;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public double getValue() {
        return value;
    }

    public boolean isPassed() {
        return value >= 3.00;
    }
}