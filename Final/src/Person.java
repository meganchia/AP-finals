public class Person {
    private String name;

    public Person(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public String displayName() {
        String s = "Welcome Mr/Ms. " + name + "!";
        return s;
    }

}
