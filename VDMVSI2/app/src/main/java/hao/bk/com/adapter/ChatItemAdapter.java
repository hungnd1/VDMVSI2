package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.ChatActivity;
import hao.bk.com.common.ChatFilter;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.models.IFilter;
import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.FragmentChatPage;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder> implements IFilter{

    public ArrayList<MemberVsiObj> listChat;
    ChatFilter filter;
    FragmentChatPage frmContainter;
    Context context;
    ToastUtil toastUtil;

    public ChatItemAdapter(FragmentChatPage frmContainter, ArrayList<MemberVsiObj> listChat) {
        this.listChat = listChat;
        this.filter = new ChatFilter(listChat);
        this.frmContainter = frmContainter;
        context = frmContainter.getContext();
        toastUtil = new ToastUtil(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void filter(CharSequence cs) {
        listChat.clear();
        listChat.addAll(filter.filter(cs));
        notifyDataSetChanged();
    }

    @Override
    public void updateFilter() {
        this.filter = new ChatFilter(listChat);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberVsiObj obj = listChat.get(position);
        holder.tvEmail.setText(obj.getUserName());
        holder.index = position;
        holder.tvLastMes.setText(obj.getLastMessage().getContent());
        holder.tvLastDate.setText(TextUtils.toSimpleDate(obj.getLastMessage().getCdate()));
        try {
            Picasso.with(context).load(obj.getUrlThumnails())
                    .placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default)
                    .into(holder.avatarUser);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return listChat != null ? listChat.size() : 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lnlRoot;
        TextView tvEmail;
        TextView tvLastMes;
        TextView tvLastDate;
        int index;
        CircleImageView avatarUser;

        ViewHolder(View itemView) {
            super(itemView);
            lnlRoot = (RelativeLayout) itemView.findViewById(R.id.lnl_root);
            lnlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, ChatActivity.class);
                        MemberVsiObj obj = listChat.get(index);
                        intent.putExtra(Config.USER_NAME_PUT, obj.getUserName());
                        context.startActivity(intent);
                   } catch (Exception e) {

                    }

                }
            });
            avatarUser = (CircleImageView) itemView.findViewById(R.id.imv_profile);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_name_user);
            tvLastMes = (TextView) itemView.findViewById(R.id.tv_last_mes);
            tvLastDate = (TextView) itemView.findViewById(R.id.tv_last_date);
        }

    }
}
