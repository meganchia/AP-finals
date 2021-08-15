import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class StudentForm extends Stage implements FormCheck {

    private String path = "./data/students.csv";
    private File students = new File(path);
    private Student students1;
    private LinkedList<Student> stuList = new LinkedList<Student>();

    private Scene studentScene;
    private Button btnAdd, btnBack;
    private Label labName, labDegree, labAdded, labWelcome;
    private TextField textName, textDegree;
    private String cornField = "-fx-background-color: #F1FAC0;";
    private boolean gotNull1;

    private TeacherForm tf = new TeacherForm();

    public StudentForm() {
        this.setTitle("Students");

        //initialize linked list
        try {
            readFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        labName = new Label("Enter Name:");
        textName = new TextField();
        labDegree = new Label("Enter Degree:");
        textDegree = new TextField();
        labAdded = new Label();
        labAdded.setTextFill(Color.RED);
        labAdded.setVisible(false);
        labWelcome = new Label();
        labWelcome.setVisible(false);


        btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String name = textName.getText();
            String degree = textDegree.getText();
            gotNull1 = checkNull();
            labAdded.setVisible(true);
            labWelcome.setVisible(true);

            //error message
            if (gotNull1) {
                labAdded.setText("Please fill in all the fields.");
            }

            //adds to txt and list
            else {
                Student stu1 = new Student(name, degree);
                stuList.add(stu1);

                try {
                    writeTo();
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                labAdded.setText("Successfully added!");
                labWelcome.setText(stu1.displayName());
            }
        });

        btnBack = new Button("Back");
        btnBack.setOnAction(e -> {
            labAdded.setVisible(false);
            labWelcome.setVisible(false);
            this.hide();
        });

        //set button styles
        tf.setBtnStyle(btnAdd, 30);
        tf.setBtnStyle(btnBack, 30);

        //button HBox
        HBox myHBox = new HBox(btnAdd, btnBack);
        myHBox.setSpacing(10);
        myHBox.setPadding(new Insets(10));
        myHBox.setAlignment(Pos.CENTER);

        VBox myVBox = new VBox(labName, textName, labDegree, textDegree, myHBox, labAdded, labWelcome);
        myVBox.setAlignment(Pos.CENTER);
        myVBox.setPadding(new Insets(40));
        myVBox.setSpacing(10);
        myVBox.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

        studentScene = new Scene(myVBox, 400, 400);
        this.setResizable(false);
        this.setScene(studentScene);
    }

    //checks for empty textfields
    @Override
    public boolean checkNull() {
        boolean gotNull = false;

        if (textName.getText().isEmpty() | textDegree.getText().isEmpty()) {
            gotNull = true;
        }

        return gotNull;
    }

    //reads students.csv
    public void readFile() throws IOException {
        int totP = tf.getTotP(path);
        Scanner scan;

        try {
            scan = new Scanner(students);

            for (int k = 1; k <= totP; k++) {
                String aLine = scan.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(",");
                String name = sline.next();
                String deg = sline.next();

                students1 = new Student(name, deg);
                stuList.add(students1);
                sline.close();
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File to read " + students + " not found!");
        }

    }

    //writes to students.csv
    public void writeTo() throws IOException {
        String name;
        String deg;
        String oldPath = "./data/oldstudents.csv";
        students.renameTo(new File(oldPath));
        students = new File (oldPath);
        PrintWriter newStudents = new PrintWriter(path);

        for (int k = 0; k < stuList.size(); k++) {
            name = stuList.get(k).getName();
            deg = stuList.get(k).getDegree();

            newStudents.println(name + "," + deg);
        }

        newStudents.close();
        students = new File(path);
        File oldStudents = new File (oldPath);
        oldStudents.delete();
    }

    public void addStudentList(Student s) {
        stuList.add(s);
    }

    public void showStage() {
        this.show();
    }
}
