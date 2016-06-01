package hao.bk.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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
        holder.avatarUser.setImageResource(R.drawable.ic_avatar);
        ChatObj obj = listChat.get(position);

        holder.index = position;
        if(obj.isItsMe()){
            holder.tvMyChat.setText(obj.getContent());
            holder.tvMyChat.setVisibility(View.VISIBLE);
            holder.avatarUser.setVisibility(View.GONE);
            holder.tvYourChat.setVisibility(View.GONE);

        }else {
            holder.tvMyChat.setVisibility(View.GONE);
            holder.avatarUser.setVisibility(View.VISIBLE);
            holder.tvYourChat.setVisibility(View.VISIBLE);
            holder.tvYourChat.setText(obj.getContent());
            if(obj.getBmpAvatar() != null)
                holder.avatarUser.setImageBitmap(obj.getBmpAvatar());
            else
                holder.avatarUser.setImageBitmap(getDefaultAvar());
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
        CircleImageView avatarUser;

        ViewHolder(View itemView) {
            super(itemView);
            avatarUser = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvYourChat = (TextView)itemView.findViewById(R.id.tv_your_chat);
            tvMyChat = (TextView)itemView.findViewById(R.id.tv_my_chat);
        }

    }
}
