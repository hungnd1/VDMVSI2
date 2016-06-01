package hao.bk.com.models;

/**
 * Created by T430 on 4/23/2016.
 */
public class DeliveryObj extends NewsObj {

    private String nameUser;
    private String urlAvarUser;
    private String urlThumnails;

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setUrlAvarUser(String urlAvarUser) {
        this.urlAvarUser = urlAvarUser;
    }

    public String getUrlAvarUser() {
        return urlAvarUser;
    }

    public void setUrlThumnails(String urlThumnails) {
        this.urlThumnails = urlThumnails;
    }

    public String getUrlThumnails() {
        return urlThumnails;
    }
}
