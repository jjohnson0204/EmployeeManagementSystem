package com.example.employeemanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.security.spec.ECField;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


// FXML Variables


public class DashboardController implements Initializable {
    @FXML
    private AnchorPane main_form;
    @FXML
    private TableColumn<EmployeeData, String> addEmployee_col_phoneNum;
    @FXML
    private TableColumn<EmployeeData, String> addEmployee_col_lastName;
    @FXML
    private Button addEmployee_addBtn;
    @FXML
    private TextField salary_salary;
    @FXML
    private Label salary_position;
    @FXML
    private BarChart<?, ?> home_chart;
    @FXML
    private Button addEmployee_btn;
    @FXML
    private ComboBox<?> addEmployee_gender;
    @FXML
    private ImageView addEmployee_image;
    @FXML
    private TableColumn<EmployeeData, String> salary_col_lastName;
    @FXML
    private TableView<EmployeeData> salary_tableView;
    @FXML
    private Button logout;
    @FXML
    private Button addEmployee_clearBtn;
    @FXML
    private Button addEmployee_importBtn;
    @FXML
    private Button salary_clearBtn;
    @FXML
    private TableColumn<EmployeeData, String> salary_col_salary;
    @FXML
    private AnchorPane addEmployee_form;
    @FXML
    private TableColumn<EmployeeData, String> salary_col_firstName;
    @FXML
    private TableColumn<EmployeeData, String> addEmployee_col_gender;
    @FXML
    private TextField addEmployee_firstName;
    @FXML
    private Button close;
    @FXML
    private Label home_totalInactive;
    @FXML
    private TableColumn<EmployeeData, String> addEmployee_col_position;
    @FXML
    private AnchorPane home_form;
    @FXML
    private TextField addEmployee_lastName;
    @FXML
    private Button minimize;
    @FXML
    private TableColumn<EmployeeData, String> salary_col_employeeID;
    @FXML
    private Button salary_btn;
    @FXML
    private Label home_totalEmployees;
    @FXML
    private TableView<EmployeeData> addEmployee_tableView;
    @FXML
    private TableColumn<EmployeeData, Date> addEmployee_col_hireDate;
    @FXML
    private TableColumn<EmployeeData, String> salary_col_position;
    @FXML
    private Label salary_firstName;
    @FXML
    private TextField addEmployee_phoneNum;
    @FXML
    private Button addEmployee_deleteBtn;
    @FXML
    private TextField addEmployee_search;
    @FXML
    private TextField addEmployee_employeeID;
    @FXML
    private Label salary_lastName;
    @FXML
    private TableColumn<EmployeeData, Integer> addEmployee_col_employeeID;
    @FXML
    private AnchorPane salary_form;
    @FXML
    private Button home_btn;
    @FXML
    private Button salary_updateBtn;
    @FXML
    private ComboBox<?> addEmployee_position;
    @FXML
    private Label home_totalActive;
    @FXML
    private Button addEmployee_updateBtn;
    @FXML
    private TextField salary_employeeID;
    @FXML
    private TableColumn<EmployeeData, String> addEmployee_col_firstName;
    @FXML
    private Label username;


    // Variables


    private Connection connection;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;


    // Fields and List


    private String[] positionOnList = {
            "Engineer I", "Engineer II", "Senior Engineer I","Senior Engineer II","Tech Lead",
            "Staff Engineer", "Senior Staff Engineer", "Chief Architect", "Engineering",
            "Engineering Director", "VP of Engineering", "CTO"
    };
    private String[] genderList = {
            "Male", "Female", "Transgender","Gender-Neutral",
            "Non-Binary", "Agender", "Pangender"
    };
    private ObservableList<EmployeeData> salaryList;
    public ObservableList<EmployeeData> salaryListData() {

        ObservableList<EmployeeData> listData = FXCollections.observableArrayList();

        String query = "SELECT * FROM employee_info";

        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            EmployeeData employeeData;

            while (result.next()) {
                employeeData = new EmployeeData(
                        result.getInt("employee_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("position"),
                        result.getDouble("salary"));
                listData.add(employeeData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<EmployeeData> addEmployeeList;
    public ObservableList<EmployeeData> addEmployeeListData() {
        ObservableList<EmployeeData> listData = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee";

        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();
            EmployeeData employeeData;

            while (result.next()) {
                employeeData = new EmployeeData(
                        result.getInt("employee_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("gender"),
                        result.getString("phone_num"),
                        result.getString("position"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(employeeData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }


    // Home Functions


    public void homeChart() {
         home_chart.getData().clear();

         String query = "" +
                 "SELECT salary, count(id) " +
                 "FROM employee_info " +
                 "GROUP BY salary " +
                 "ORDER BY salary " +
                 "ASC LIMIT 7"
         ;

         try {
             XYChart.Series chart = new XYChart.Series();

             prepare = connection.prepareStatement(query);
             result = prepare.executeQuery();

             while (result.next()) {
                 chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
             }
             home_chart.getData().add(chart);

         }
         catch (Exception e) {
             e.printStackTrace();
         }
    }
    public void homeTotalEmployees() {
        String query = "SELECT count(*) FROM employee";
        int countData = 0;

        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            while(result.next()) {
                countData = result.getInt("count");
            }

            home_totalEmployees.setText(String.valueOf(countData));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void activeEmployees() {
        String query = "SELECT count(*) FROM employee_info WHERE salary != '0.0'";
        int countData = 0;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);

            while(result.next()) {
                countData = result.getInt("count");
            }

            home_totalActive.setText(String.valueOf(countData));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void inactiveEmployees() {
        String query = "SELECT count(*) FROM employee_info WHERE salary = '0.0'";
        int countData = 0;

        try {
            prepare = connection.prepareStatement(query);
            result = prepare.executeQuery();

            while(result.next()) {
                countData = result.getInt("count");
            }

            home_totalInactive.setText(String.valueOf(countData));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Add Employee Functions
    public void addEmployeeAdd() {

//        Date date = ;
//        java.sql.Date queryDate = new java.sql.Date(date.getTime());
        String query = "INSERT INTO employee (employee_id, first_name, last_name, gender, phone_num, position, image) VALUES(?,?,?,?,?,?,?)";

        try {
            Alert alert;
            if (       addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_firstName.getText().isEmpty()
                    || addEmployee_lastName.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || GetData.path == null || GetData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields.");
                alert.showAndWait();
            }
            else {
                String check = "SELECT employee_id FROM employee WHERE employee_id = '"
                        + addEmployee_employeeID.getText() + "'";

                statement = connection.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID: " + addEmployee_employeeID.getText() + " already exist!");
                    alert.showAndWait();
                }
                else {
                    prepare = connection.prepareStatement(query);
                    prepare.setInt(1,  Integer.parseInt(addEmployee_employeeID.getText()));
                    prepare.setString(2, addEmployee_firstName.getText());
                    prepare.setString(3, addEmployee_lastName.getText());
                    prepare.setString(4, (String)addEmployee_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(5, addEmployee_phoneNum.getText());
                    prepare.setString(6, (String)addEmployee_position.getSelectionModel().getSelectedItem());

                    String uri = GetData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);
//                    prepare.setString(8, String.valueOf(queryDate));
                    prepare.executeUpdate();

                    String insertInfo = "INSERT INTO employee_info "
                            + "(employee_id, first_name, last_name, position, salary) "
                            + "VALUES(?,?,?,?,?)";

                    prepare = connection.prepareStatement(insertInfo);
                    prepare.setInt(1, Integer.parseInt(addEmployee_employeeID.getText()));
                    prepare.setString(2, addEmployee_firstName.getText());
                    prepare.setString(3, addEmployee_lastName.getText());
                    prepare.setString(4, (String)addEmployee_position.getSelectionModel().getSelectedItem());
                    prepare.setDouble(5, Double.parseDouble("0.0"));
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addEmployeeUpdate() {
        String uri = GetData.path;
        uri = uri.replace("\\", "\\\\");
        String query = "UPDATE employee SET first_name = '"
                + addEmployee_firstName.getText() + "', last_name = '"
                + addEmployee_lastName.getText() + "', gender = '"
                + addEmployee_gender.getSelectionModel().getSelectedItem() + "', position = '"
                + addEmployee_position.getSelectionModel().getSelectedItem() + "', phone_num = '"
                + addEmployee_phoneNum.getText() + "', image = '"
                + uri + "' WHERE employee_id ='"
                + addEmployee_employeeID.getText() + "'";

        try {
            Alert alert;
            if (       addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_firstName.getText().isEmpty()
                    || addEmployee_lastName.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || GetData.path == null || GetData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields.");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Employee ID: " + addEmployee_employeeID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connection.createStatement();
                    statement.executeUpdate(query);

                    double salary = 0;

                    String checkData = "SELECT * FROM employee_info WHERE employee_id = '"
                            + addEmployee_employeeID.getText() + "'";

                    prepare = connection.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    while (result.next()) {
                        salary = result.getDouble("salary");
                    }

                    String updateInfo = "UPDATE employee_info SET first_name = '"
                            + addEmployee_firstName.getText() + "', last_name = '"
                            + addEmployee_lastName.getText() + "', position = '"
                            + addEmployee_position.getSelectionModel().getSelectedItem() + "', salary = '"
                             + salary + "' WHERE employee_id ='"
                            + addEmployee_employeeID.getText() + "'";

                    prepare = connection.prepareStatement(updateInfo);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Updated Successfully!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addEmployeePositionList() {
        List<String> listPositions = new ArrayList<>();

        for (String data : positionOnList) {
            listPositions.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listPositions);
        addEmployee_position.setItems(listData);
    }
    public void addEmployeeDelete() {
        String query = "DELETE FROM employee WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

        try {
            Alert alert;
            if (       addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_firstName.getText().isEmpty()
                    || addEmployee_lastName.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || GetData.path == null || GetData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields.");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Employee ID: " + addEmployee_employeeID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connection.createStatement();
                    statement.executeUpdate(query);

                    String deleteInfo = "DELETE FROM employee_info WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

                    prepare = connection.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Deleted Successfully!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addEmployeeReset() {
        addEmployee_employeeID.setText("");
        addEmployee_firstName.setText("");
        addEmployee_lastName.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_phoneNum.setText("");
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_image.setImage(null);
        GetData.path = "";
    }
    public void addEmployeeInsertImage() {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            GetData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 100, 150, false, true);
            addEmployee_image.setImage(image);
        }
    }
    public void addEmployeeSearch() {

        FilteredList<EmployeeData> filter = new FilteredList<>(addEmployeeList, e-> true);

        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateEmployeeData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateEmployeeData.getEmployeeID().toString().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getPhoneNum().toLowerCase().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                }
                else if (predicateEmployeeData.getDate().toString().contains(searchKey)) {
                    return true;
                }else {
                    return false;
                }
            });
        });

        SortedList<EmployeeData> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortedList);
    }
    public void addEmployeeGenderList() {
        List<String> listGender = new ArrayList<>();

        for (String data : genderList) {
            listGender.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listGender);
        addEmployee_gender.setItems(listData);
    }
    public void addEmployeeShowListData() {
        addEmployeeList = addEmployeeListData();
        addEmployee_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        addEmployee_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmployee_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmployee_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmployee_col_hireDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeeList);
    }
    public void addEmployeeSelect() {
        EmployeeData employeeData = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) { return;}

        addEmployee_employeeID.setText(String.valueOf(employeeData.getEmployeeID()));
        addEmployee_firstName.setText(String.valueOf(employeeData.getFirstName()));
        addEmployee_lastName.setText(String.valueOf(employeeData.getLastName()));
        addEmployee_phoneNum.setText(String.valueOf(employeeData.getPhoneNum()));

        GetData.path = employeeData.getImage();

        String uri = "file:" + employeeData.getImage();
        image = new Image(uri, 100, 125, false, true);
        addEmployee_image.setImage(image);
    }


    // Salary Form Functions


    public void salaryUpdate() {
        String query = "UPDATE employee_info SET salary = '" + salary_salary.getText()
                + "' WHERE employee_id = '" + salary_employeeID.getText() + "'";

        try {
            Alert alert;
            if (salary_employeeID.getText().isEmpty()
                    || salary_firstName.getText().isEmpty()
                    || salary_lastName.getText().isEmpty()
                    || salary_position.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select employee first.");
                alert.showAndWait();
            }
            else {
                statement = connection.createStatement();
                statement.executeUpdate(query);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                salaryShowListData();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void salaryReset() {
        salary_employeeID.setText("");
        salary_firstName.setText("");
        salary_lastName.setText("");
        salary_position.setText("");
        salary_salary.setText("");
    }
    public void salaryShowListData() {
        salaryList = salaryListData();

        salary_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        salary_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        salary_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        salary_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        salary_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        salary_tableView.setItems(salaryList);
    }
    public void salarySelect() {
        EmployeeData employeeData = salary_tableView.getSelectionModel().getSelectedItem();
        int num = salary_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) { return;}

        salary_employeeID.setText(String.valueOf(employeeData.getEmployeeID()));
        salary_firstName.setText(employeeData.getFirstName());
        salary_lastName.setText(employeeData.getLastName());
        salary_position.setText(employeeData.getPosition());
        salary_salary.setText(String.valueOf(employeeData.getSalary()));
    }


    // Button and Flow Functions

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
    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            addEmployee_btn.setStyle("-fx-background-color: transparent");
            salary_btn.setStyle("-fx-background-color: transparent");

            displayUsername();
            homeTotalEmployees();
            activeEmployees();
            inactiveEmployees();
            homeChart();
        }
        else if (event.getSource() == addEmployee_btn){
            home_form.setVisible(false);
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);

            addEmployee_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            salary_btn.setStyle("-fx-background-color: transparent");

            displayUsername();
            addEmployeeGenderList();
            addEmployeePositionList();
            addEmployeeSearch();
        }
        else if (event.getSource() == salary_btn){
            home_form.setVisible(false);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);

            salary_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
            home_btn.setStyle("-fx-background-color: transparent");
            addEmployee_btn.setStyle("-fx-background-color: transparent");

            displayUsername();
            salaryShowListData();
        }
    }
    public void defaultNav() {
        home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a4368, #28966c)");
    }


    // Connection Functions


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        defaultNav();

        homeTotalEmployees();
        activeEmployees();
        inactiveEmployees();
        homeChart();

        addEmployeeShowListData();
        addEmployeeGenderList();
        addEmployeePositionList();

        salaryShowListData();
    }
}
