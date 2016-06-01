package hao.bk.com.models;

/**
 * Created by T430 on 4/23/2016.
 */
public class NewsObj {
    private String title;
    private String intros;
    private String time = "5 phút trước";
    private String content;

    private String setUrlNew;

    public void setSetUrlNew(String setUrlNew) {
        this.setUrlNew = setUrlNew;
    }

    public String getSetUrlNew() {
        return setUrlNew;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIntros(String intros) {
        this.intros = intros;
    }

    public String getIntros() {
        return intros;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
