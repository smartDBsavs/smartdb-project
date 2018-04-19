package smartdbsavs.com.smartdb_demo.holders;

import android.net.Uri;

import smartdbsavs.com.smartdb_demo.SplashScreen;

/**
 * Created by hi on 16-03-2018.
 */

public class LogsDTO {

    String id;
    String titleMessage;
    String label;
    String dateTime;
    String imageUrl;
    String status;
    String btn_status;
    Uri uri;


    public LogsDTO() {
    }

    public LogsDTO(String id, String titleMessage, String label, String dateTime, String imageUrl, String status, String btn_status) {
        this.id = id;
        this.titleMessage = titleMessage;
        this.label = label;
        this.dateTime = dateTime;
        this.imageUrl = imageUrl;
        this.status = status;
        this.btn_status = btn_status;
    }

    public LogsDTO(String titleMessage, String label, String dateTime, String imageUrl, String status, String btn_status) {
        this.titleMessage = titleMessage;
        this.label = label;
        this.dateTime = dateTime;
        this.imageUrl = imageUrl;
        this.status = status;
        this.btn_status = btn_status;
    }

    public String getBtn_status() {
        return btn_status;
    }

    public void setBtn_status(String btn_status) {
        this.btn_status = btn_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleMessage() {
        return titleMessage;
    }

    public void setTitleMessage(String titleMessage) {
        this.titleMessage = titleMessage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "LogsDTO{" +
                "id='" + id + '\'' +
                ", titleMessage='" + titleMessage + '\'' +
                ", label='" + label + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status='" + status + '\'' +
                ", btn_status='" + btn_status + '\'' +
                ", uri=" + uri +
                '}';
    }
}
