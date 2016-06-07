package hao.bk.com.common;

import java.util.ArrayList;

import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.models.NewsObj;

public class NewsFilter {

    private ArrayList<NewsObj> originalList;

    public NewsFilter(ArrayList<NewsObj> originalList) {
        this.originalList = new ArrayList<>();
        this.originalList.addAll(originalList);
    }

    public ArrayList<NewsObj> filter(CharSequence constraint) {
        ArrayList<NewsObj> filteredList = new ArrayList<>();
        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            for (NewsObj obj : originalList) {
                if (obj.getTitle().contains(filterPattern)||obj.getContent().contains(filterPattern)) {
                    filteredList.add(obj);
                }
            }
        }
        return filteredList;
    }

}