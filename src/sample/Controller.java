package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdk.nashorn.internal.runtime.Debug;


import java.time.LocalDate;

public class Controller {

    @FXML private TextField textboxName;
    @FXML private DatePicker dtpPicker;
    @FXML private TableView<Birthday> lstBirthdays;
    @FXML private TableColumn<Birthday,String> NameColumn;
    @FXML private TableColumn<Birthday,LocalDate> BirthdayColumn;

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        BirthdayColumn.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());
        refresh();
        lstBirthdays.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));

    }


    public Controller(){
        }

    public void AddClicked(MouseEvent mouseEvent) {
        Main.addBirthday(textboxName.getText(),dtpPicker.getValue());
        refresh();
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = lstBirthdays.getSelectionModel().getSelectedIndex();
        lstBirthdays.getItems().remove(selectedIndex);
    }

    public void refresh() {
        lstBirthdays.setItems(Main.getBirthdaysList());
    }

    private void showDetails(Birthday birthday) {
        if (birthday != null) {

            textboxName.setText(birthday.getName());
            dtpPicker.setValue(birthday.getBirthday());

            System.out.println(birthday.getName() + "  " + birthday.getNewAge() + " "  + birthday.daysToNextBirthday());

        } else {

            textboxName.setText("");
            dtpPicker.setValue(null);
        }

    }


    public void saveClicked(MouseEvent mouseEvent) {
        Main.save();
    }
}
