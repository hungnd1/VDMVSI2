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
    Bitmap myAvatar,yourAvatar;

    public ChatMessageAdapter(Context context, ArrayList<ChatObj> listChat, Bitmap myAvatar, Bitmap yourAvatar){
        this.listChat = listChat;
        this.context = context;
        this.myAvatar = myAvatar;
        this.yourAvatar = yourAvatar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_friend_chat, parent, false);
        viewHolder = new ViewHolder(view);
        viewHolder.myAvatar.setImageBitmap(myAvatar);
        viewHolder.yourAvatar.setImageBitmap(yourAvatar);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatObj obj = listChat.get(position);
        ChatObj lastObj = position < listChat.size()-1 ? listChat.get(position+1) : null;
        if(obj.isItsMe()){
            holder.tvMyChat.setText(obj.getContent());
            holder.tvMyChat.setVisibility(View.VISIBLE);
            holder.myAvatar.setVisibility(View.INVISIBLE);
            if (lastObj == null || !lastObj.isItsMe()) {
                holder.myAvatar.setVisibility(View.VISIBLE);
            }
            holder.yourAvatar.setVisibility(View.INVISIBLE);
            holder.tvYourChat.setVisibility(View.INVISIBLE);
        }else {
            holder.tvMyChat.setVisibility(View.INVISIBLE);
            holder.myAvatar.setVisibility(View.INVISIBLE);
            holder.yourAvatar.setVisibility(View.INVISIBLE);
            if (lastObj == null || lastObj.isItsMe()) {
                holder.yourAvatar.setVisibility(View.VISIBLE);
            }
            holder.tvYourChat.setText(obj.getContent());
            holder.tvYourChat.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return listChat != null ? listChat.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvYourChat;
        TextView tvMyChat;
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
