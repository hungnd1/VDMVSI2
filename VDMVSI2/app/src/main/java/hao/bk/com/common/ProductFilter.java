package hao.bk.com.common;

import java.util.ArrayList;

import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.models.ProductObj;

public class ProductFilter {

    private ArrayList<ProductObj> originalList;

    public ProductFilter(ArrayList<ProductObj> originalList) {
        this.originalList = new ArrayList<>();
        this.originalList.addAll(originalList);
    }

    public ArrayList<ProductObj> filterCompany(CharSequence constraint) {
        ArrayList<ProductObj> filteredList = new ArrayList<>();
        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            for (ProductObj obj : originalList) {
                if (obj.getCompanyId().equals(constraint.toString())) {
                    filteredList.add(obj);
                }
            }
        }
        return filteredList;
    }

}