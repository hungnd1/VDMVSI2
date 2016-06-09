package hao.bk.com.models;

/**
 * Created by T430 on 4/23/2016.
 */
public class ProductObj extends NewsObj {

    private String name;
    private String urlAvarUser;
    private String urlThumnails;
    private String company;

    public void setName(String nameUser) {
        this.name = nameUser;
    }

    public String getName() {
        return name;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
