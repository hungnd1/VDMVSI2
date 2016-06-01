package hao.bk.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.models.DeliveryObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.FragmentDelivery;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class DeliveryItemAdapter extends  RecyclerView.Adapter<DeliveryItemAdapter.ViewHolder> {

    ArrayList<NewsObj> listNews;
    FragmentDelivery frmContainer;
    Context context;
    ToastUtil toastUtil;
    public DeliveryItemAdapter(FragmentDelivery frmContainer, ArrayList<NewsObj> listNews){
        this.listNews = listNews;
        this.frmContainer = frmContainer;
        this.context = frmContainer.getContext();
        toastUtil = frmContainer.toastUtil;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_news_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivAvatar.setImageResource(R.drawable.ic_avatar);
        holder.ivNews.setImageResource(R.drawable.nom);
        DeliveryObj obj = (DeliveryObj) listNews.get(position);
        holder.tvUserName.setText(obj.getNameUser());
        holder.tvTime.setText(obj.getTime());
        holder.tvTitle.setText(obj.getTitle());
        holder.index = position;
        //holder.tvDescription.setText(obj.getDescription());
    }

    @Override
    public int getItemCount() {
        if(listNews != null)
            return listNews.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        CircleImageView ivAvatar;
        TextView tvUserName;
        TextView tvTime;
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivNews;
        int index;
        Button btnShow, btnEdit, btnDel;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_news);
            ivAvatar = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvUserName = (TextView)itemView.findViewById(R.id.tv_name_user);
            tvTime = (TextView)itemView.findViewById(R.id.tv_time);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_descript);
            ivNews = (ImageView)itemView.findViewById(R.id.imv_thumbnails);
            btnShow = (Button)itemView.findViewById(R.id.btn_show);
            btnEdit = (Button)itemView.findViewById(R.id.btn_edit);
            btnDel = (Button)itemView.findViewById(R.id.btn_del);
            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    showNews(index);
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    editNews(index);
                }
            });
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    delNews(index);
                }
            });
        }
        public void showNews(int index){
            toastUtil.showToast("Hiển thị chi tiết!");
        }
        public void editNews(int index){
            toastUtil.showToast("Sửa thông tin!");
        }
        public void delNews(int index){
            listNews.remove(index);
            frmContainer.adapter.notifyDataSetChanged();
        }
    }
}
