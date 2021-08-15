import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MySchool extends Application {

    private Scene mainScene;
    private Button btnTeacher, btnStudent;
    private Label labLogin;

    private TeacherForm tf = new TeacherForm();
    private StudentForm sf = new StudentForm();

    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle("School");

        //mainstage
        labLogin = new Label("CREATE");
        labLogin.setFont(new Font(30));
        labLogin.setAlignment(Pos.CENTER);

        btnTeacher = new Button("Teacher");
        btnTeacher.setOnAction(e -> {
            mainStage.hide();
            tf.showStage();
        });
        btnStudent = new Button("Student");
        btnStudent.setOnAction(e -> {
            mainStage.hide();
            sf.showStage();
        });

        //sets button style
        tf.setBtnStyle(btnTeacher, 30);
        tf.setBtnStyle(btnStudent, 30);

        //on other scene hiding
        tf.setOnHidden(e -> {
            mainStage.show();
        });
        sf.setOnHidden(e -> {
            mainStage.show();
        });

        HBox myHBox = new HBox(btnTeacher, btnStudent);
        myHBox.setSpacing(10);
        myHBox.setAlignment(Pos.CENTER);

        VBox myVBox = new VBox(labLogin, myHBox);
        myVBox.setAlignment(Pos.CENTER);
        myVBox.setPadding(new Insets(40));
        myVBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        mainScene = new Scene(myVBox, 400, 300);
        mainStage.setResizable(false);
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    public static void main(String args[]) {
        Application.launch(args);
    }
}
