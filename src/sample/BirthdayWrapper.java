package sample;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Dominik on 11.08.2017.
 */
@XmlRootElement(name = "birthdays")
public class BirthdayWrapper {
    private List<Birthday> birthdays;

    @XmlElement(name = "birthday")
    public List<Birthday> getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(List<Birthday> birthdays) {
        this.birthdays = birthdays;
    }

}
