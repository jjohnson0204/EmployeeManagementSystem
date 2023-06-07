package com.example.employeemanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmployeeBoardController extends PersistenceHandler implements Initializable {
    // FXML Forms
    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane home_form;
    @FXML
    private AnchorPane clockin_form;
    @FXML
    private AnchorPane clockout_form;
    @FXML
    private AnchorPane schedule_form;
    @FXML
    private AnchorPane employee_profile_form;

    //FXML Buttons
    @FXML
    private Button home_btn;
    @FXML
    private Button clockin_btn;
    @FXML
    private Button clockout_btn;
    @FXML
    private Button schedule_btn;
    @FXML
    private Button employee_profile_btn;
    @FXML
    private Button logout;

    // FXML Labels
    @FXML
    private Label username;

    // Clock IN/OUT FXML elements
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField employeeIdField1;
    @FXML
    private TextField clockInId;
    @FXML
    private Button clockInButton;
    @FXML
    private Button clockOutButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label statusLabel1;

    //Scheduler FXML elements
    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;


    // Variables
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    ZonedDateTime dateFocus;
    ZonedDateTime today;



    // Clock-In Form Functions
    // Clock in action
    public void clockIn() {
        // Get the employee id from the text field
        String employeeId = employeeIdField1.getText();

        // Check if the employee id is valid
        if (employeeId == null || employeeId.isEmpty()) {
            statusLabel1.setText("Please enter a valid employee id.");
            return;
        }

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        Instant time = now.toInstant(ZoneOffset.UTC);
        // Insert a new record into the clock table with the employee id and the clock in time
        try {
            prepare = connection.prepareStatement("INSERT INTO clock (employee_id, clock_in) VALUES (?, ?)");
            prepare.setString(1, (employeeId));
            prepare.setTimestamp(2, Timestamp.from(time));
            prepare.executeUpdate();
            statusLabel1.setText("You have clocked in at " + dateTime);
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel1.setText("An error occurred while clocking in.");
        }
    }


    // Clout Out Form Functions
    // Clock out action
    public void clockOut() {
        // Get the employee id from the text field
        String employeeId = employeeIdField.getText();

        // Check if the employee id is valid
        if (employeeId == null || employeeId.isEmpty()) {
            statusLabel.setText("Please enter a valid employee id.");
            return;
        }

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        String dateTime = dtf.format(now);
        Instant time = now.toInstant(ZoneOffset.UTC);

        // Update the last record of the clock table with the same employee id and set the clock out time
        try {
            prepare = connection.prepareStatement("UPDATE clock SET clock_out = ? WHERE employee_id = ? AND clock_out IS NULL");
            prepare.setTimestamp(1, Timestamp.from(time));
            prepare.setString(2, String.valueOf((Integer.valueOf(employeeId))));
            int rows = prepare.executeUpdate();
            if (rows > 0) {
                statusLabel.setText("You have clocked out at " + dateTime);
            } else {
                statusLabel.setText("You have not clocked in yet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred while clocking out.");
        }
    }


    // Scheduler Form Functions

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // List activities for the month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();

        // check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6 ) - strokeWidth -spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1) + (7*i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    // On ... Click
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getUsername() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                // On text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color: Gray");
        stackPane.getChildren().add(calendarActivityBox);
    }
    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            }
            else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }
    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27) + 1, 16, 0, 0, 0, dateFocus.getZone());
            calendarActivities.add(new CalendarActivity(time,"Pixel",111111));
        }
        return createCalendarMap(calendarActivities);
    }


    // Buttons and Flow Functions
    private double x = 0;
    private double y = 0;
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout:");
        Optional<ButtonType> option = alert.showAndWait();

        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        System.exit(0);
    }
    public void minimize() {
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void displayUsername() {
        username.setText(GetData.username);
    }
    public void displayEmployeeId() {
        employeeIdField.setText(String.valueOf(GetData.id));
        employeeIdField1.setText(String.valueOf(GetData.id));
    }
    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            clockin_form.setVisible(false);
            clockout_form.setVisible(false);
            schedule_form.setVisible(false);
            employee_profile_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            clockin_btn.setStyle("-fx-background-color: transparent");
            clockout_btn.setStyle("-fx-background-color: transparent");
            schedule_btn.setStyle("-fx-background-color: transparent");
            employee_profile_btn.setStyle("-fx-background-color: transparent");

            displayUsername();
        }
        else if (event.getSource() == clockin_btn){
            home_form.setVisible(false);
            clockin_form.setVisible(true);
            clockout_form.setVisible(false);
            schedule_form.setVisible(false);
            employee_profile_form.setVisible(false);

            clockin_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            clockout_btn.setStyle("-fx-background-color: transparent");
            schedule_btn.setStyle("-fx-background-color: transparent");
            employee_profile_btn.setStyle("-fx-background-color: transparent");

            displayUsername();
        }
        else if (event.getSource() == clockout_btn){
            home_form.setVisible(false);
            clockin_form.setVisible(false);
            clockout_form.setVisible(true);
            schedule_form.setVisible(false);
            employee_profile_form.setVisible(false);

            clockout_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            clockin_btn.setStyle("-fx-background-color: transparent");
            schedule_btn.setStyle("-fx-background-color: transparent");
            employee_profile_btn.setStyle("-fx-background-color: transparent");

            displayUsername();

        }
        else if (event.getSource() == schedule_btn){
            home_form.setVisible(false);
            clockin_form.setVisible(false);
            clockout_form.setVisible(false);
            schedule_form.setVisible(true);
            employee_profile_form.setVisible(false);

            schedule_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            clockin_btn.setStyle("-fx-background-color: transparent");
            clockout_btn.setStyle("-fx-background-color: transparent");
            employee_profile_btn.setStyle("-fx-background-color: transparent");

            displayUsername();

        }
        else if (event.getSource() == employee_profile_btn){
            home_form.setVisible(false);
            clockin_form.setVisible(false);
            clockout_form.setVisible(false);
            schedule_form.setVisible(false);
            employee_profile_form.setVisible(true);

            employee_profile_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            clockin_btn.setStyle("-fx-background-color: transparent");
            schedule_btn.setStyle("-fx-background-color: transparent");
            clockout_btn.setStyle("-fx-background-color: transparent");

            displayUsername();

        }
    }
    public void defaultNav() {
        home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        displayEmployeeId();
        defaultNav();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }
}
