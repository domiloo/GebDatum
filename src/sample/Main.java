package sample;

import com.sun.javafx.collections.SortableList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Main extends Application {

    @XmlElement(name="birthday")
    private static ObservableList<Birthday> birthdays = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
        primaryStage.setTitle("Geburtsdatum");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();


    }




    public static void main(String[] args) {

        load();
        //System.out.println(args[0]);

        if (args.length > 0) {
            save();
            System.out.println("Saved HTML and finished");
            System.exit(0);
        } else {
            launch(args);
        }

    }

    public static void addBirthday(String name, LocalDate  date) {
        if (name != "" && date != null) {
            birthdays.add(new Birthday(name,date));

        }
        birthdays.sort(Birthday::compareTo);
    }

    public static List<Birthday> getListOfNextBirthdays(int nextDays) {
        ObservableList<Birthday> bdays = FXCollections.observableArrayList();
        for ( Birthday bday : getBirthdaysList())
        {
            if (bday.daysToNextBirthday() <= nextDays) {
                bdays.add(bday);
            }
        }
        bdays.sort(Birthday::compareTo);
        return bdays;
    }

    @XmlElements({ @XmlElement(name = "Birthday", type = Birthday.class) })
    public static ObservableList<Birthday> getBirthdaysList() {
        return birthdays;
    }

    public static void save() {
        Path path = Paths.get("bdays.xml");


        try {
            JAXBContext context = JAXBContext.newInstance(BirthdayWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            BirthdayWrapper wrapper = new BirthdayWrapper();
            wrapper.setBirthdays(birthdays);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, path.toFile());

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + path.toString());

            alert.showAndWait();
        }

              //Export HTML-HauptFile:

        try{

            FileWriter fstream = new FileWriter("C:/gebdatum/gebdatum.htm");
            BufferedWriter out = new BufferedWriter(fstream);
            String color = "black";
            out.write("<html><head><title>Geburtstage</title></head><body><!--Created by Dominik Looser (C)2017-->" );

            out.newLine();
            out.write("<h3>Geburtstage in den folgenden 30 Tagen</h3><h4><a href=\"C:\\gebdatum\\gebAll.htm\">Alle anzeigen</a></h4><ul>");
            out.newLine();

            for ( Birthday bday : getListOfNextBirthdays(30))
            {
                if (bday.daysToNextBirthday() == 0) {
                    color = "red";
                }else{
                    color = "black";
                }
                out.write("<li><font color=\"" + color + "\"><b>" + bday.getBirthday().getDayOfMonth() + "." + bday.getBirthday().getMonthValue() + ". :</b> " + bday.getName() + " (" + bday.getNewAge()+ ")</font></li>");
                out.newLine();

            }
            out.write("</ul>\n" +
                    "<p><a href=\"https://login.live.com/login.srf?wa=wsignin1.0&rpsnv=11&ct=1289155034&rver=6.1.6206.0&wp=MBI&wreply=http:%2F%2Fco114w.col114.mail.live.com%2Fdefault.aspx%3Frru%3Dinbox&lc=2055&id=64855&mkt=de-ch&cbcxt=mai&snsc=1\">Zu den Mails (Hotmail)</a>\n" +
                    "</body></html>");
            out.newLine();

            out.close();
        }  catch (Exception e){//Catch exception if any
            System.err.println("Error while exporting HTML: " + e.getMessage());
        }

        //Export HTML-File (alle):

        try{

            FileWriter fstream = new FileWriter("C:/gebdatum/gebAll.htm");
            BufferedWriter out = new BufferedWriter(fstream);
            String color = "black";
            out.write("<html><head><title>Alle Geburtstage</title></head><body><!--Created by Dominik Looser (C)2017-->" );
            out.newLine();

            out.write("<h3>Alle Geburtstage</h3><ul>");
            out.newLine();

            for ( Birthday bday : getBirthdaysList())
            {
                out.write("<li><font color=\"" + color + "\"><b>" + bday.getBirthday().getDayOfMonth() + "." + bday.getBirthday().getMonthValue() + "." + bday.getBirthday().getYear() + " :</b> " + bday.getName() + " (" + bday.getNewAge()+ ")</font></li>");
                out.newLine();

            }
            out.write("</ul>\n" +
                    "</body></html>");
            out.newLine();

            out.close();
        }  catch (Exception e){//Catch exception if any
            System.err.println("Error while exporting HTML: " + e.getMessage());
        }

    }

    public static void load(){
        Path path = Paths.get("bdays.xml");

            try {
                JAXBContext context = JAXBContext
                        .newInstance(BirthdayWrapper.class);
                Unmarshaller um = context.createUnmarshaller();

                // Reading XML from the file and unmarshalling.
                BirthdayWrapper wrapper = (BirthdayWrapper) um.unmarshal(path.toFile());

                birthdays.clear();
                birthdays.addAll(wrapper.getBirthdays());
                birthdays.sort(Birthday::compareTo);


            } catch (Exception e) { // catches ANY exception
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not load data");
                alert.setContentText("Could not load data from file:\n" + path);

                alert.showAndWait();
            }


    }

}
