package bg.tu_varna.sit.f24621656.models;

public class Grade {
    private final Discipline name;
    private final double value;

    public Grade(Discipline name, double value) {
        this.name = name;
        this.value = value;
    }

    public Discipline getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
