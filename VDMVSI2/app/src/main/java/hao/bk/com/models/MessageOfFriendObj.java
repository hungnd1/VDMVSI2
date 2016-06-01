package hao.bk.com.models;

import java.util.ArrayList;

/**
 * Created by T430 on 4/25/2016.
 */
public class MessageOfFriendObj {
    private String urlAvatarFriend;
    private String idFriend;
    ArrayList<ChatObj> listMessage;

    public MessageOfFriendObj(){
        listMessage = new ArrayList<>();
    }

    public void setUrlAvatarFriend(String urlAvatarFriend) {
        this.urlAvatarFriend = urlAvatarFriend;
    }

    public String getUrlAvatarFriend() {
        return urlAvatarFriend;
    }
    public void addMessageToList(ChatObj obj){
        this.listMessage.add(obj);
    }
    public void setListMessage(ArrayList<ChatObj> listMessage) {
        this.listMessage = listMessage;
    }

    public ArrayList<ChatObj> getListMessage() {
        return listMessage;
    }
}
