public class TeachingAssistant extends Teacher {

    private String status;

    public TeachingAssistant(String n, String sub, String s) {
        super(n, sub);
        status = s;
    }

    public String getStatus() {
        return status;
    }
}
