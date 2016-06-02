package hao.bk.com.models;

import android.graphics.Bitmap;

/**
 * Created by T430 on 4/23/2016.
 */
public class ChatObj {
    private int id;
    private String from;
    private String to;
    private String content;
    private String cdate;
    private boolean itsMe;
    private int isRun;
    public void setId(int id) {
        this.id = id;
    }

    public void setItsMe(boolean itsMe) {
        this.itsMe = itsMe;
    }

    public boolean isItsMe() {
        return itsMe;
    }

    public int getId() {
        return id;
    }


    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCdate() {
        return cdate;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setIsRun(int isRun) {
        this.isRun = isRun;
    }

    public int getIsRun() {
        return isRun;
    }

}
