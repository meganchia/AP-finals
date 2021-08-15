public class Student extends Person {

    private String degree;

    public Student(String n, String d) {
        super(n);
        degree = d;
    }

    public String getDegree() {
        return degree;
    }
}
