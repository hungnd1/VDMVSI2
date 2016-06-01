package hao.bk.com.models;

/**
 * Created by T430 on 4/24/2016.
 */
public class UserObj {
    private int id;
    private String userName;
    private String firtName;
    private String LastName;
    private int gender;
    private String urlAvatar;
    private String address;
    private String email;
    private String facebook;
    private String twitter;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setFirtName(String firtName) {
        this.firtName = firtName;
    }

    public String getFirtName() {
        return firtName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTwitter() {
        return twitter;
    }
}
