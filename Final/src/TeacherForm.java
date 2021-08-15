import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TeacherForm extends Stage implements FormCheck {

    private String tPath = "./data/teachers.csv";
    private String taPath = "./data/teachingassistants.csv";
    private File teachers = new File(tPath);
    private File assistants = new File(taPath);
    private Teacher teacher1;
    private TeachingAssistant assistant1;
    private LinkedList<Teacher> tList = new LinkedList<Teacher>();
    private LinkedList<TeachingAssistant> taList = new LinkedList<TeachingAssistant>();

    private Scene teacherScene;
    private Button btnAdd, btnBack;
    private Label labName, labSubject, labAdded, labWelcome;
    private TextField textName, textSubject;
    private CheckBox checkAssistant;
    private ComboBox options;
    private String cornField = "-fx-background-color: #F1FAC0;";
    private boolean gotNull1;

    public TeacherForm() {
        this.setTitle("Teachers");

        //initialize linked lists
        try {
            readFile();
            readAFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        labName = new Label("Enter Name:");
        textName = new TextField();
        labSubject = new Label("Enter Subject:");
        textSubject = new TextField();
        labAdded = new Label();
        labAdded.setVisible(false);
        labAdded.setTextFill(Color.RED);
        labWelcome = new Label();
        labWelcome.setVisible(false);

        //checkbox for teaching assistant
        checkAssistant = new CheckBox("Teaching Assistant");
        options = new ComboBox();
        options.getItems().addAll("Part-Time", "Full-Time");
        options.setVisible(false);

        checkAssistant.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (checkAssistant.isSelected()) {
                    options.setVisible(true);
                }
                else {
                    options.setVisible(false);
                }
            }
        });

        btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String name = textName.getText();
            String subject = textSubject.getText();
            String status = (String) options.getValue();
            gotNull1 = checkNull();
            labAdded.setVisible(true);
            labWelcome.setVisible(true);

            //error message
            if (gotNull1) {
                labAdded.setText("Please fill in all the fields.");
            }

            //adds to teachingassistants.csv
            else if (checkAssistant.isSelected()) {
                TeachingAssistant ta1 = new TeachingAssistant(name, subject, status);
                taList.add(ta1);
                try {
                    writeToAssistant();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                labAdded.setText("Successfully added!");
                labWelcome.setText(ta1.displayName());
            }

            //adds to teachers.csv and list
            else {
                Teacher t1 = new Teacher(name, subject);
                tList.add(t1);
                try {
                    writeToTeacher();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                labAdded.setText("Successfully added!");
                labWelcome.setText(t1.displayName());
            }
        });

        btnBack = new Button("Back");
        btnBack.setOnAction(e -> {
            this.hide();
            labAdded.setVisible(false);
            labWelcome.setVisible(false);
        });

        //set button styles
        setBtnStyle(btnAdd, 30);
        setBtnStyle(btnBack, 30);

        //button HBox
        HBox myHBox = new HBox(btnAdd, btnBack);
        myHBox.setSpacing(10);
        myHBox.setPadding(new Insets(10));
        myHBox.setAlignment(Pos.CENTER);

        VBox myVBox = new VBox(labName, textName, labSubject, textSubject, checkAssistant, options, myHBox, labAdded, labWelcome);
        myVBox.setAlignment(Pos.CENTER);
        myVBox.setPadding(new Insets(40));
        myVBox.setSpacing(15);
        myVBox.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

        teacherScene = new Scene(myVBox, 400, 400);
        this.setResizable(false);
        this.setScene(teacherScene);
    }

    //checks for empty textfields
    @Override
    public boolean checkNull() {
        boolean gotNull = false;

        if (textName.getText().isEmpty() | textSubject.getText().isEmpty()) {
            gotNull = true;
        }

        return gotNull;
    }

    //get the total number of people from file
    public int getTotP(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
        int noOfLines = lines.size();
        int totPeople = noOfLines;
        return totPeople;
    }

    //reads teachers.csv
    public void readFile() throws IOException {
        int totP = getTotP(tPath);
        Scanner scan;

        try {
            scan = new Scanner(teachers);

            for (int k = 1; k <= totP; k++) {
                String aLine = scan.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(",");
                String name = sline.next();
                String sub = sline.next();

                teacher1 = new Teacher(name, sub);
                tList.add(teacher1);
                sline.close();
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File to read " + teachers + " not found!");
        }
    }

    //reads teachingassistants.csv
    public void readAFile() throws IOException {
        int totTA = getTotP(taPath);
        Scanner scan;

        try {
            scan = new Scanner(assistants);

            for (int k = 1; k <= totTA; k++) {
                String aLine = scan.nextLine();
                Scanner sline = new Scanner(aLine);
                sline.useDelimiter(",");
                String name = sline.next();
                String sub = sline.next();
                String stat = sline.next();

                assistant1 = new TeachingAssistant(name, sub, stat);
                taList.add(assistant1);
                sline.close();
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File to read " + teachers + " not found!");
        }

    }

    //writes to teachers.csv
    public void writeToTeacher() throws FileNotFoundException {
        String name;
        String sub;
        String oldPath = "./data/oldteachers.csv";
        teachers.renameTo(new File(oldPath));
        teachers = new File (oldPath);
        PrintWriter newTeachers = new PrintWriter(tPath);

        //teachers
        for (int k = 0; k < tList.size(); k++) {
            name = tList.get(k).getName();
            sub = tList.get(k).getSubject();

            newTeachers.println(name + "," + sub);
        }
        newTeachers.close();

        teachers = new File(tPath);
        File oldTeachers = new File (oldPath);
        oldTeachers.delete();
    }

    //writes to teachingassistants.csv
    public void writeToAssistant() throws FileNotFoundException {
        String name;
        String sub;
        String stat;
        String oldPath = "./data/oldtas.csv";
        assistants.renameTo(new File(oldPath));
        assistants = new File (oldPath);
        PrintWriter newTeachers = new PrintWriter(taPath);

        for (int i = 0; i < taList.size(); i++) {
            name = taList.get(i).getName();
            sub = taList.get(i).getSubject();
            stat = taList.get(i).getStatus();

            newTeachers.println(name + "," + sub + "," + stat);
        }
        newTeachers.close();

        assistants = new File(taPath);
        File oldAssistants = new File (oldPath);
        oldAssistants.delete();
    }

    public void setBtnStyle(Button b, int s) {
        int width = s * 4;
        int height = s * 2;
        b.setStyle(cornField);
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #FA8072; -fx-text-fill: #FFFFFF"));
        b.setOnMouseExited(e -> b.setStyle(cornField));
        b.setPrefSize(width, height);
        b.setFont(new Font(20));
    }

    public void addTeacherList(Teacher t) {
        tList.add(t);
    }

    public void addAssistantList(TeachingAssistant a) {
        taList.add(a);
    }

    public void showStage() {
        this.show();
    }

}
