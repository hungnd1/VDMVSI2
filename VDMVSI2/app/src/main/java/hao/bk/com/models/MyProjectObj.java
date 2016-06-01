package hao.bk.com.models;

/**
 * Created by T430 on 4/25/2016.
 */
public class MyProjectObj {

    private String tileProject;
    private String majorProject;
    private long fromDate;
    private long endDate;
    private long cDate;
    private String content;
    public void setTileProject(String tileProject) {
        this.tileProject = tileProject;
    }

    public String getTileProject() {
        return tileProject;
    }

    public void setMajorProject(String majorProject) {
        this.majorProject = majorProject;
    }

    public String getMajorProject() {
        return majorProject;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setcDate(long cDate) {
        this.cDate = cDate;
    }

    public long getcDate() {
        return cDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return tileProject + " - " + majorProject + " - " + fromDate + " - " + endDate + content;
    }
}
