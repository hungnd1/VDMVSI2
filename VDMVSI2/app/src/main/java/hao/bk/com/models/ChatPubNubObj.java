package hao.bk.com.models;

/**
 * Created by T430 on 4/27/2016.
 */
public class ChatPubNubObj {
    private String author;
    private String mess;
    private String thumnails;
    private long time;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getMess() {
        return mess;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setThumnails(String thumnails) {
        this.thumnails = thumnails;
    }

    public String getThumnails() {
        return thumnails;
    }
}
