package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.ChatActivity;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.models.MessageOfFriendObj;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.vdmvsi.FragmentChatPage;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class ChatItemAdapter extends  RecyclerView.Adapter<ChatItemAdapter.ViewHolder> implements Filterable {

    ArrayList<MemberVsiObj> listChat;
    FragmentChatPage frmContainter;
    Context context;
    ToastUtil toastUtil;

    public ChatItemAdapter(FragmentChatPage frmContainter, ArrayList<MemberVsiObj> listChat){
        this.listChat = listChat;
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
    public Filter getFilter() {
        return new ChatFilter(this, listChat);
    }

    private  class ChatFilter extends Filter {

        private final ChatItemAdapter adapter;

        private final List<MemberVsiObj> originalList;

        private final ArrayList<MemberVsiObj> filteredList;

        private ChatFilter(ChatItemAdapter adapter, ArrayList<MemberVsiObj> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final MemberVsiObj obj : originalList) {
                    if (obj.getUserName().contains(filterPattern)) {
                        filteredList.add(obj);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0)
                return;
            adapter.listChat.clear();
            adapter.listChat.addAll((ArrayList<MemberVsiObj>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberVsiObj obj = listChat.get(position);
        holder.tvEmail.setText(obj.getUserName());
        holder.tvPhone.setText(obj.getPhone());
        holder.index = position;
        holder.avatarUser.setImageResource(R.drawable.ic_avatar_default);
        try {
            Picasso.with(context).load(obj.getUrlThumnails()).resize(120, 60).into(holder.avatarUser);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(listChat != null)
            return listChat.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lnlRoot;
        TextView tvEmail;
        TextView tvPhone;
        int index;
        CircleImageView avatarUser;

        ViewHolder(View itemView) {
            super(itemView);
            lnlRoot = (RelativeLayout)itemView.findViewById(R.id.lnl_root);
            lnlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(context, ChatActivity.class);
                        MemberVsiObj obj = listChat.get(index);
                        intent.putExtra(Config.USER_NAME_PUT, obj.getUserName());
                        intent.putExtra(Config.URL_THUMNAILS_PUT, obj.getUrlThumnails());
                        context.startActivity(intent);
                    }catch (Exception e){

                    }

                }
            });
            avatarUser = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvEmail = (TextView)itemView.findViewById(R.id.tv_name_user);
            tvPhone = (TextView)itemView.findViewById(R.id.tv_phone);
        }

    }
}
