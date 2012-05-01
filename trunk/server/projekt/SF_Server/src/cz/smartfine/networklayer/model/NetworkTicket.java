package cz.smartfine.networklayer.model;

import cz.smartfine.android.model.Law;
import cz.smartfine.android.model.Ticket;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Třída modelu PL
 *
 * @author Pavel Brož
 *
 */
public class NetworkTicket implements Serializable {

    private int id;
    /**
     * UID verze serializace
     */
    private static final long serialVersionUID = 1L;
    /**
     * Služební číslo policisty který PL nahrál - číslo odznaku policisty
     */
    private int uploaderBadgeNumber;
    /**
     * Služební číslo - číslo odznaku policisty
     */
    private int badgeNumber;
    /**
     * Město
     */
    private String city;
    /**
     * Datum a čas
     */
    private Date date;
    /**
     * Místo události (např.: číslo lampy)
     */
    private String location;
    /**
     * Zda je pohyblivé DZ
     */
    private boolean moveableDZ;
    /**
     * Mezinárodní poznávací značka
     */
    private String mpz;
    /**
     * Číslo ulice
     */
    private int number;
    /**
     * Fotodokumentace události
     */
    private List<byte[]> photos = new ArrayList<byte[]>();
    /**
     * Státní poznávací značka
     */
    private String spz;
    /**
     * Barva státní poznávací značky
     */
    private String spzColor;
    /**
     * Ulice
     */
    private String street;
    /**
     * Zda je vozidlo odtaženo
     */
    private boolean tow;
    /**
     * Značka automobilu
     */
    private String vehicleBrand;
    /**
     * Typ automobilu
     */
    private String vehicleType;
    /**
     * Zákon popisující přestupek
     */
    private Law law = new Law();
    /**
     * Zda byl už lístek vytištěn
     */
    private boolean printed;

    public NetworkTicket() {
        super();
    }

    /**
     * Kopírovací konstruktor.
     */
    public NetworkTicket(Ticket ticket) {
        super();

        //zkopíruje atributy//
        this.badgeNumber = ticket.getBadgeNumber();
        this.city = ticket.getCity();
        this.date = ticket.getDate();
        this.location = ticket.getLocation();
        this.moveableDZ = ticket.isMoveableDZ();
        this.mpz = ticket.getMpz();
        this.number = ticket.getNumber();
        this.spz = ticket.getSpz();
        this.spzColor = ticket.getSpzColor();
        this.street = ticket.getStreet();
        this.tow = ticket.isTow();
        this.vehicleBrand = ticket.getVehicleBrand();
        this.vehicleType = ticket.getVehicleType();
        this.law = ticket.getLaw();
        this.printed = ticket.isPrinted();

        if (ticket.getPhotos() != null) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            //projde soubory a načte jejich obsah//
            for (File photo : ticket.getPhotos()) {
                try {
                    fis = new FileInputStream(photo);
                    bis = new BufferedInputStream(fis);

                    long fileSize = photo.length(); //zjistí délku souboru
                    if (fileSize > 0) {
                        byte[] photoBytes = new byte[(int) fileSize]; //bytvoří buffer na data
                        bis.read(photoBytes); //načte soubor

                        photos.add(photoBytes); //přidá soubor do seznamu
                    }

                    fis.close();
                    bis.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUploaderBadgeNumber() {
        return uploaderBadgeNumber;
    }

    public void setUploaderBadgeNumber(int uploaderBadgeNumber) {
        this.uploaderBadgeNumber = uploaderBadgeNumber;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isMoveableDZ() {
        return moveableDZ;
    }

    public void setMoveableDZ(boolean moveableDZ) {
        this.moveableDZ = moveableDZ;
    }

    public String getMpz() {
        return mpz;
    }

    public void setMpz(String mpz) {
        this.mpz = mpz;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<byte[]> getPhotos() {
        return photos;
    }

    public void setPhotos(List<byte[]> photos) {
        this.photos = photos;
    }

    public String getSpz() {
        return spz;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public String getSpzColor() {
        return spzColor;
    }

    public void setSpzColor(String spzColor) {
        this.spzColor = spzColor;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public boolean isTow() {
        return tow;
    }

    public void setTow(boolean tow) {
        this.tow = tow;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Law getLaw() {
        return law;
    }

    public void setLaw(Law law) {
        this.law = law;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public int getCollection() {
        return law.getCollection();
    }

    public void setCollection(int collection) {
        this.law.setCollection(collection);
    }

    public int getLawNumber() {
        return law.getLawNumber();
    }

    public void setLawNumber(int lawNumber) {
        this.law.setLawNumber(lawNumber);
    }

    public String getLetter() {
        return law.getLetter();
    }

    public void setLetter(String letter) {
        this.law.setLetter(letter);
    }

    public int getParagraph() {
        return law.getParagraph();
    }

    public void setParagraph(int paragraph) {
        this.law.setParagraph(paragraph);
    }

    public int getRuleOfLaw() {
        return law.getRuleOfLaw();
    }

    public void setRuleOfLaw(int ruleOfLaw) {
        this.law.setRuleOfLaw(ruleOfLaw);
    }
}