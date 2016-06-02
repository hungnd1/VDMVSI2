package hao.bk.com.common;

import java.util.ArrayList;

import hao.bk.com.models.MemberVsiObj;

public class ChatFilter {

        private ArrayList<MemberVsiObj> originalList;

        public ChatFilter(ArrayList<MemberVsiObj> originalList) {
            this.originalList = new ArrayList<>();
            this.originalList.addAll(originalList);
        }

        public ArrayList<MemberVsiObj> filter(CharSequence constraint) {
            ArrayList<MemberVsiObj> filteredList = new ArrayList<>();
            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MemberVsiObj obj : originalList) {
                    if (obj.getUserName().contains(filterPattern)) {
                        filteredList.add(obj);
                    }
                }
            }
            return filteredList;
        }

    }