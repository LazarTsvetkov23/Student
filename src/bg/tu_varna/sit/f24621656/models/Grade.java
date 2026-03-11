package bg.tu_varna.sit.f24621656.models;

public class Grade {
    private final Discipline disciplineName;
    private final double value;

    public Grade(Discipline disciplineName, double value) {
        this.disciplineName = disciplineName;
        this.value = value;
    }

    public Discipline getDisciplineName() {
        return disciplineName;
    }

    public double getValue() {
        return value;
    }
}
