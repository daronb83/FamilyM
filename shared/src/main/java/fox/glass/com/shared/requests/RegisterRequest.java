package fox.glass.com.shared.requests;

/**
 * Models a JSON request for the RegisterService
 */
public class RegisterRequest implements Request {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Models a JSON request for the RegisterService
     *
     * @param userName the user's username
     * @param password the user's password
     * @param email the user's email address
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param gender the user's gender, either "m" or "f"
     */
    public RegisterRequest(String userName, String password, String email, String firstName,
                           String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public RegisterRequest(){
        userName = "";
        password = "";
        email = "";
        firstName = "";
        lastName = "";
        gender = "";
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
