package hao.bk.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.R;
import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.models.ChatObj;

/**
 * Created by T430 on 4/26/2016.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{
     ArrayList<ChatObj> listChat;
    Context context;
    public ChatMessageAdapter( Context context, ArrayList<ChatObj> listChat){
        this.listChat = listChat;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_friend_chat, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    public Bitmap getDefaultAvar(){
        return BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_avatar);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.yourAvatar.setImageResource(R.drawable.ic_avatar);
        ChatObj obj = listChat.get(position);
        ChatObj lastObj = position < listChat.size()-1 ? listChat.get(position+1) : null;
        holder.index = position;
        if(obj.isItsMe()){
            holder.tvMyChat.setText(obj.getContent());
            holder.tvMyChat.setVisibility(View.VISIBLE);
            if (lastObj == null || !lastObj.isItsMe()) {
                holder.myAvatar.setVisibility(View.VISIBLE);
            } else{
                holder.myAvatar.setVisibility(View.INVISIBLE);
            }
            if (position == 0){
                holder.tvMyChat.setText(Html.fromHtml(obj.getContent()+"<br><font size=\"5\" color=#e1e1e1>"+obj.getCdate()+"</font>"), TextView.BufferType.SPANNABLE);
            }
            holder.yourAvatar.setVisibility(View.INVISIBLE);
            holder.tvYourChat.setVisibility(View.INVISIBLE);
            if(obj.getBmpAvatar() != null)
                holder.myAvatar.setImageBitmap(obj.getBmpAvatar());
            else
                holder.myAvatar.setImageBitmap(getDefaultAvar());
        }else {
            holder.tvMyChat.setVisibility(View.INVISIBLE);
            holder.myAvatar.setVisibility(View.INVISIBLE);
            if (lastObj == null || lastObj.isItsMe()) {
                holder.yourAvatar.setVisibility(View.VISIBLE);
            }else{
                holder.yourAvatar.setVisibility(View.INVISIBLE);
            }
            holder.tvYourChat.setText(obj.getContent());
            if (position == 0){
                holder.tvYourChat.setText(Html.fromHtml("<font color=#cc0029>hello</font> <font color=#ffcc00>world</font>"), TextView.BufferType.SPANNABLE);
            }
            holder.tvYourChat.setVisibility(View.VISIBLE);
            if(obj.getBmpAvatar() != null)
                holder.yourAvatar.setImageBitmap(obj.getBmpAvatar());
            else
                holder.yourAvatar.setImageBitmap(getDefaultAvar());
        }
    }
    @Override
    public int getItemCount() {
        if(listChat != null)
            return listChat.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvYourChat;
        TextView tvMyChat;
        int index;
        CircleImageView yourAvatar;
        CircleImageView myAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            yourAvatar = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            myAvatar = (CircleImageView)itemView.findViewById(R.id.imv_my_profile);
            tvYourChat = (TextView)itemView.findViewById(R.id.tv_your_chat);
            tvMyChat = (TextView)itemView.findViewById(R.id.tv_my_chat);
        }

    }
}
