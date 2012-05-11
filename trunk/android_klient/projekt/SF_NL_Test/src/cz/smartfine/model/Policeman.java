package cz.smartfine.model;

import java.io.Serializable;

/**
 * Obecná třída policisty.
 * @author Pavel Brož
 */
public class Policeman implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String firstName;
    private String lastName;
    private int badgeNumber;

    public Policeman() {
        super();
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        if (firstName != null && lastName != null && firstName.length() != 0 && lastName.length() != 0){
            return String.valueOf(badgeNumber) + " - " + firstName + " " + lastName;
        }else{
            return String.valueOf(badgeNumber);
        }
    }
    
    
}
