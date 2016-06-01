package hao.bk.com.models;

/**
 * Created by T430 on 4/22/2016.
 */
public class CoporateNewsObj extends NewsObj {


    private int id;
    private int carId;
    private String urlAvar;
    private String userName;
    private String phoneNumber;
    private int like;
    private int unlike;
    private long cDate;
    private long fromDate;
    private long EndDate;
    private int isActive;
    private int status;
    private String comment;

    @Override
    public void setContent(String content) {
        super.setContent(content);
    }

    @Override
    public String getContent() {
        return super.getContent();
    }

    public void setcDate(long cDate) {
        this.cDate = cDate;
    }

    public long getcDate() {
        return cDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setEndDate(long endDate) {
        EndDate = endDate;
    }

    public long getEndDate() {
        return EndDate;
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

    public void setUrlAvar(String urlAvar) {
        this.urlAvar = urlAvar;
    }

    public String getUrlAvar() {
        return urlAvar;
    }

    public void setNameUser(String nameUser) {
        this.userName = nameUser;
    }

    public String getNameUser() {
        return userName;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getIsLike() {
        return like;
    }

    public int getIsUnlike() {
        return unlike;
    }
    public void setUnlike(int unlike) {
        this.unlike = unlike;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
