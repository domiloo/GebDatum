package sample;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



/**
 * Created by Dominik on 19.07.2017.
 */
@XmlType(name = "Birthday")

public class Birthday implements Comparable<Birthday>{

    private final ObjectProperty<LocalDate> birthday;
    private final StringProperty name;


    public Birthday() {
        this(null,null);
    }


    public Birthday(String name, LocalDate birthday) {
        this.name = new SimpleStringProperty(name);
        this.birthday = new SimpleObjectProperty<LocalDate>(birthday);

    }

    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name.get();
    }

    public void setName(String Name) {
        this.name.set(Name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlAttribute(name = "birthday", required = true)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }


    public int getNewAge() {

        int birthYear = getBirthday().getYear();

        int newAge =  nextBirthday().getYear() - birthYear;

        return newAge;
    }

    public LocalDate nextBirthday() {
        LocalDate nextBday = getBirthday().withYear(LocalDate.now().getYear());
        if (nextBday.compareTo(LocalDate.now())<0) { //already had birthday this year
            nextBday = nextBday.plusYears(1);
        }

        return nextBday;
    }
    public long daysToNextBirthday() {

        return ChronoUnit.DAYS.between(LocalDate.now(),nextBirthday());

    }


    @Override
    public int compareTo(Birthday o) {
        return getBirthday().withYear(LocalDate.now().getYear()).compareTo(o.getBirthday().withYear(LocalDate.now().getYear()));
    }
}
