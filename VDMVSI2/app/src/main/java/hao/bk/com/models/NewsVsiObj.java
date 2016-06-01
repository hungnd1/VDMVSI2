package hao.bk.com.models;

/**
 * Created by T430 on 4/23/2016.
 */
public class NewsVsiObj extends NewsObj {

    private String urlThumnails;
    private int id;
    private int carId;
    private int cateId;
    private int numView;
    private String cDate;
    private String mDate;
    private int isActive;
    public void setUrlThumnails(String urlThumnails) {
        this.urlThumnails = urlThumnails;
    }

    public String getUrlThumnails() {
        return urlThumnails;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getCateId() {
        return cateId;
    }

    public void setNumView(int numView) {
        this.numView = numView;
    }

    public int getNumView() {
        return numView;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcDate() {
        return cDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return isActive;
    }
}
