package hao.bk.com.models;

/**
 * Created by T430 on 4/25/2016.
 */
public class MemberVsiObj {
    private int id;
    private String urlThumnails;
    private String userName;
    private String phone;
    private ChatObj lastMessage;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUrlThumnails(String urlThumnails) {
        this.urlThumnails = urlThumnails;
    }

    public String getUrlThumnails() {
        return urlThumnails;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public ChatObj getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatObj lastMessage) {
        this.lastMessage = lastMessage;
    }
}
