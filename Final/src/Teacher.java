public class Teacher extends Person {

    private String subject;

    public Teacher(String n, String sub) {
        super(n);
        subject = sub;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String displayName() {
        String s = "Welcome Professor " + super.getName() + "!";
        return s;
    }
}
