package ro.presidential.election;

import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @SequenceGenerator(
            name = "persons_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "persons_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private String emailAdress;
    private String firstName;
    private String lastName;
    private int age;
    private String profession;
    private String status = "voter";
    private String bibliography = "";
    private String motto = "";
    private int nrOfVotes = 0;
    private boolean hasVoted = false;

    public Person() { }

    public Person(String username, String password, String phoneNumber, String emailAdress, String firstName, String lastName, int age, String profession) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.emailAdress = emailAdress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profession = profession;
        this.hasVoted = false;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public void incrementVotes() {
        this.nrOfVotes++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getNrOfVotes() {
        return nrOfVotes;
    }

    public void setNrOfVotes(int nrOfVotes) {
        this.nrOfVotes = nrOfVotes;
    }
}
