package entity;

public class Doctor {
    private int id;
    private String fullName;
    private String specialty;
    private int expYears;
    private double baseSalary;
    private String password;

    public Doctor() {
    }

    public Doctor(int id, String fullName, String specialty, int expYears, double baseSalary, String password) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
        this.expYears = expYears;
        this.baseSalary = baseSalary;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getExpYears() {
        return expYears;
    }

    public void setExpYears(int expYears) {
        this.expYears = expYears;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", expYears=" + expYears +
                ", baseSalary=" + baseSalary +
                ", password='" + password + '\'' +
                '}';
    }
}
