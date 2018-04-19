package smartdbsavs.com.smartdb_demo.holders;

/**
 * Created by hi on 20-02-2018.
 */

public class RegistrationDetailDTO {
    String userName;
    String emailId;
    String registrationId;

    public RegistrationDetailDTO(String userName, String emailId, String registrationId) {
        this.userName = userName;
        this.emailId = emailId;
        this.registrationId = registrationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
