package hao.bk.com.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.ArrayList;

import hao.bk.com.models.ChatObj;
import hao.bk.com.models.ChatPubNubObj;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.LCareObj;
import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.models.NewsVsiObj;
import hao.bk.com.models.UserObj;
import hao.bk.com.utils.TextUtils;

/**
 * Created by T430 on 4/24/2016.
 */
public class JsonCommon {

    public static ArrayList<CoporateNewsObj> getCoporateNews(JsonArray jsonArray){
        ArrayList<CoporateNewsObj> list = new ArrayList<>();
        if(jsonArray == null)
            return list;
        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject object = (JsonObject)jsonArray.get(i);
            if(object == null)
                continue;
            CoporateNewsObj newsObj = new CoporateNewsObj();
            if(object.has("id")) {
                try {
                    newsObj.setId(object.get("id").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("car_id")) {
                try {
                    newsObj.setCarId(object.get("car_id").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("title")) {
                try {
                    newsObj.setTitle(object.get("title").getAsString());
                }catch (Exception e){

                }
            }
            if(object.has("intro")){
                try {
                    newsObj.setIntros(object.get("intro").getAsString());
                }catch (Exception e){

                }
            }
            if(object.has("content")) {
                try {
                    newsObj.setContent(object.get("content").getAsString());
                } catch (Exception e) {
                }
            }
            if(object.has("cdate")) {
                try {
                    newsObj.setcDate(object.get("cdate").getAsLong());
                } catch (Exception e) {
                }
            }
            if(object.has("from_date")) {
                try {
                    newsObj.setFromDate(object.get("from_date").getAsLong());
                } catch (Exception e) {
                }
            }
            if(object.has("end_date")) {
                try {
                    newsObj.setEndDate(object.get("end_date").getAsLong());
                } catch (Exception e) {
                }
            }
            if(object.has("username")) {
                try {
                    newsObj.setNameUser(object.get("username").getAsString());
                } catch (Exception e) {
                }
            }
            if(object.has("c_comment")) {
                try {
                    newsObj.setComment(object.get("c_comment").getAsString());
                } catch (Exception e) {
                }
            }
            if(object.has("like")) {
                try {
                    newsObj.setLike(object.get("like").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("unlike")) {
                try {
                    newsObj.setUnlike(object.get("unlike").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("isactive")){
                try {
                    newsObj.setIsActive(object.get("isactive").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("status")) {
                try {
                    newsObj.setStatus(object.get("status").getAsInt());
                } catch (Exception e) {
                }
            }
            if(object.has("tel")){
                try {
                    newsObj.setPhoneNumber(object.get("tel").getAsString());
                } catch (Exception e) {
                }
            }
            list.add(newsObj);
        }
        return list;
    }

    public static ArrayList<NewsVsiObj> getNewsVsi(JsonArray jsonArray) {
        ArrayList<NewsVsiObj> list = new ArrayList<>();
        if (jsonArray == null)
            return list;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = (JsonObject) jsonArray.get(i);
            if(object == null)
                continue;
            NewsVsiObj newsObj = new NewsVsiObj();
            if (object.has("id")) {
                try {
                    newsObj.setId(object.get("id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("cate_id")) {
                try {
                    newsObj.setCateId(object.get("cate_id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("car_id")) {
                try {
                    newsObj.setCarId(object.get("car_id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("title")) {
                try {
                    newsObj.setTitle(object.get("title").getAsString());
                }catch (Exception e){

                }
            }
            if (object.has("intro")){
                try {
                    newsObj.setIntros(object.get("intro").getAsString());
                }catch (Exception e){

                }
            }
            if(object.has("fulltext")){
                try {
                    newsObj.setContent(object.get("fulltext").getAsString());
                }catch (Exception e){

                }
            }
            if (object.has("thumb")) {
                try {
                    newsObj.setSetUrlNew(object.get("thumb").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("view")) {
                try {
                    newsObj.setNumView(object.get("view").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("cdate")) {
                try {
                    newsObj.setcDate(object.get("cdate").getAsString());
                } catch (Exception e) {
                }

            }if (object.has("mdate"))
                try {
                    newsObj.setmDate(object.get("mdate").getAsString());
                } catch (Exception e) {
                }
            if (object.has("isactive")) {
                try {
                    newsObj.setIsActive(object.get("isactive").getAsInt());
                } catch (Exception e) {
                }
            }
            list.add(newsObj);
        }
        return list;
    }
    public static ArrayList<ChatObj> getChatTwoUser(String userName, JsonArray jsonArray) {
        ArrayList<ChatObj> list = new ArrayList<>();
        if (jsonArray == null)
            return list;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = (JsonObject) jsonArray.get(i);
            if(object == null)
                continue;
            ChatObj newsObj = new ChatObj();
            if (object.has("id")) {
                try {
                    newsObj.setId(object.get("id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("from")) {
                try {
                    newsObj.setFrom(object.get("from").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("to")) {
                try {
                    newsObj.setTo(object.get("to").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("content")) {
                try {
                    newsObj.setContent(object.get("content").getAsString());
                }catch (Exception e){

                }
            }
            if (object.has("cdate")){
                try {
                    newsObj.setCdate(object.get("cdate").getAsString());
                }catch (Exception e){

                }
            }
            if (object.has("isrun")) {
                try {
                    newsObj.setIsRun(object.get("isrun").getAsInt());
                } catch (Exception e) {
                }
            }
            if(userName.equals(newsObj.getFrom())){
                newsObj.setItsMe(true);
            }else{
                newsObj.setItsMe(false);
            }
            if (list.size()>0){
                ChatObj lastObj = list.get(list.size()-1);
                if (newsObj.isItsMe() == lastObj.isItsMe() && TextUtils.equalTime(newsObj.getCdate(),lastObj.getCdate())){
                    lastObj.setContent(lastObj.getContent()+"\n"+newsObj.getContent());
                    continue;
                }
            }
            list.add(newsObj);
        }
        return list;
    }
    public static UserObj getUserInfo(JsonArray jsonArray) {
        UserObj userObj = new UserObj();
        if (jsonArray == null)
            return userObj;
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        if(jsonObject == null)
            return userObj;
        if(jsonObject.has("id")){
            try{
                userObj.setId(jsonObject.get("id").getAsInt());
            }catch (Exception e){

            }
        }
        if(jsonObject.has("username")){
            try{
                userObj.setUserName(jsonObject.get("username").getAsString());
            }catch (Exception e){

            }
        }
        if(jsonObject.has("email")){
            try{
                userObj.setEmail(jsonObject.get("email").getAsString());
            }catch (Exception e){

            }
        }
        if(jsonObject.has("avata")){
            try{
                userObj.setUrlAvatar(jsonObject.get("avata").getAsString());
            }catch (Exception e){

            }
        }
        return userObj ;
    }
    public static void getLastMessage(JsonArray jsonArray) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        if (jsonArray == null)
//        for(int i = 0; i < jsonArray.size(); i++) {
//            JsonObject object = (JsonObject) jsonArray.get(i);
//            if (object == null)
//                continue;
//            ChatObj chatObj = new ChatObj();
//            if (object.has("id")) {
//                try {
//                    chatObj.setId(object.get("id").getAsInt());
//                } catch (Exception e) {
//                }
//            }
//            if (object.has("from")) {
//                try {
//                    chatObj.setFrom(object.get("from").getAsString());
//                } catch (Exception e) {
//                }
//            }
//            if (object.has("to")) {
//                try {
//                    chatObj.setTo(object.get("to").getAsString());
//                } catch (Exception e) {
//                }
//            }
//            if (object.has("content")) {
//                try {
//                    chatObj.setContent(object.get("content").getAsString());
//                } catch (Exception e) {
//                }
//            }
//            if (object.has("cdate")) {
//                try {
//                    chatObj.setCdate(object.get("cdate").getAsString());
//                } catch (Exception e) {
//                }
//            }
//            if (object.has("isrun")) {
//                try {
//                    chatObj.setIsRun(object.get("isrun").getAsInt());
//                } catch (Exception e) {
//                }
//            }
//            list.add(chatObj);
//        }
//        return list;
    }

    public static ArrayList<MemberVsiObj> getAllUser(String userName, JsonArray jsonArray){
        ArrayList<MemberVsiObj> list = new ArrayList<>();
        if(jsonArray == null)
            return list;
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = (JsonObject) jsonArray.get(i);
            if (object == null)
                continue;
            MemberVsiObj memObj = new MemberVsiObj();
            if (object.has("id")) {
                try {
                    memObj.setId(object.get("id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("username")) {
                try {
                    if(userName.equals(object.get("username").getAsString()))
                        continue;
                    memObj.setUserName(object.get("username").getAsString());
                } catch (Exception e) {
                }
            }
            if(object.has("tel")){
                try {
                    memObj.setPhone(object.get("tel").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("avata")) {
                try {
                    memObj.setUrlThumnails(object.get("avata").getAsString());
                } catch (Exception e) {
                }
            }
            list.add(memObj);
        }
        return list;
    }
    public static ArrayList<LCareObj> getListCare(JsonArray jsonArray) {
        ArrayList<LCareObj> list = new ArrayList<>();
        if(jsonArray == null)
            return list;
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = (JsonObject) jsonArray.get(i);
            if (object == null)
                continue;
            LCareObj memObj = new LCareObj();
            if (object.has("id")) {
                try {
                    memObj.setId(object.get("id").getAsInt());
                } catch (Exception e) {
                }
            }
            if (object.has("code")) {
                try {
                    memObj.setCode(object.get("code").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("name")) {
                try {
                    memObj.setName(object.get("name").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("intro")) {
                try {
                    memObj.setIntro(object.get("intro").getAsString());
                } catch (Exception e) {
                }
            }
            if (object.has("isactive")) {
                try {
                    memObj.setIsActive(object.get("isactive").getAsInt());
                } catch (Exception e) {
                }
            }
            list.add(memObj);
        }
        return list;
    }

    public static ChatPubNubObj getMessageChatFromPubNub(String msg){

        ChatPubNubObj chatObj = new ChatPubNubObj();
        if(TextUtils.isEmpty(msg))
            return chatObj;
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) (parser.parse(msg).getAsJsonObject()).get("mess");
        if(obj == null)
            return chatObj;
        try{
            chatObj.setAuthor(obj.get("author").getAsString());
        }catch (Exception e){

        }
        try{
            chatObj.setMess(obj.get("mess").getAsString());
        }catch (Exception e){

        }
        try{
            chatObj.setTime(obj.get("321313").getAsLong());
        }catch (Exception e){

        }
        return chatObj;
    }
}
