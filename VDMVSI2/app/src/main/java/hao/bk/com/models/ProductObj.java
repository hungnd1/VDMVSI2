package hao.bk.com.models;

/**
 * Created by T430 on 4/23/2016.
 */
public class ProductObj extends NewsObj {

    private String id;
    private String name;
    private String urlAvarUser;
    private String urlThumnails;
    private String company;
    private String companyId;
    private String pro_code;
    private String intro;
    private String fulltext;
    private String username;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPro_code() {
        return pro_code;
    }

    public void setPro_code(String pro_code) {
        this.pro_code = pro_code;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
