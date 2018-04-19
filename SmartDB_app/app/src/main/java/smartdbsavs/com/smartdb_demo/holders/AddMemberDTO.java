package smartdbsavs.com.smartdb_demo.holders;

/**
 * Created by hi on 05-03-2018.
 */

public class AddMemberDTO {

    String memid_AM;
    String userName_AM;
    String emailId_AM;


    public AddMemberDTO() {
    }


    public AddMemberDTO(String memid_AM, String userName_AM, String emailId_AM) {
        this.memid_AM = memid_AM;
        this.userName_AM = userName_AM;
        this.emailId_AM = emailId_AM;
    }

    public AddMemberDTO(String userName_AM, String emailId_AM) {
        this.userName_AM = userName_AM;
        this.emailId_AM = emailId_AM;
    }

    public String getMemid_AM() { return memid_AM; }

    public void setMemid_AM(String memid_AM) { this.memid_AM = memid_AM; }

    public String getUserName_AM() {
        return userName_AM;
    }

    public void setUserName_AM(String userName_AM) {
        this.userName_AM = userName_AM;
    }

    public String getEmailId_AM() {
        return emailId_AM;
    }

    public void setEmailId_AM(String emailId_AM) {
        this.emailId_AM = emailId_AM;
    }
}
