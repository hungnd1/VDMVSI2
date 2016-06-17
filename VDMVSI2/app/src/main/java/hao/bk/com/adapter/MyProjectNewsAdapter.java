package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.CreateProjectActivity;
import hao.bk.com.chat.ProjectDetailActivity;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.NewsFilter;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.IFilter;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.FragmentCoporateNew;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class MyProjectNewsAdapter extends  RecyclerView.Adapter<MyProjectNewsAdapter.ViewHolder> implements IFilter {
    static ArrayList<NewsObj> listNews;
    public FragmentCoporateNew frmContainer;
    DataStoreApp dataStoreApp;
    NewsFilter filter;
    public Context context;
    ToastUtil toastUtil;
    public MyProjectNewsAdapter(FragmentCoporateNew frmContainer, ArrayList<NewsObj> listNews){
        this.listNews = listNews;
        this.frmContainer = frmContainer;
        context = frmContainer.getContext();
        toastUtil = frmContainer.toastUtil;
        dataStoreApp = new DataStoreApp(frmContainer.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CoporateNewsObj obj = (CoporateNewsObj) listNews.get(position);
        if (obj.getUrlAvar() == null || obj.getUrlAvar() == "") {
            Picasso.with(context.getApplicationContext()).load(R.drawable.icon_user).transform(new CircleTransform()).into(holder.imageNews);
        } else {
            Picasso.with(context.getApplicationContext()).load(obj.getUrlAvar()).transform(new CircleTransform()).into(holder.imageNews);
        }
        if(obj.getFirstname() == "" || obj.getLastname() == ""){
            holder.tvName.setText(obj.getNameUser() + " > " + obj.getTitle());
        }else{
            holder.tvName.setText(obj.getFirstname() + " " + obj.getLastname() + " > " + obj.getTitle());
        }
        holder.tvTime.setText("Ngày "+HViewUtils.getTimeViaMiliseconds(obj.getcDate()));
        holder.tvTime.setText(HViewUtils.getTimeViaMiliseconds(obj.getcDate()));
        holder.tvTitle.setVisibility(View.GONE);
        holder.index = position;
        holder.tvDescription.setText(obj.getContent());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_project_news_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return listNews != null ? listNews.size() : 0;
    }

    @Override
    public void filter(CharSequence cs) {
        listNews.clear();
        listNews.addAll(filter.filter(cs));
        notifyDataSetChanged();
    }

    @Override
    public void updateFilter() {
        filter = new NewsFilter(listNews);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView tvTitle;
        TextView tvName;
        TextView tvTime;
        TextView tvDescription;
        CircleImageView imageNews;
        int index;
        Button btnShow, btnEdit, btnDel;

        ViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_news);
            imageNews = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvName = (TextView)itemView.findViewById(R.id.tv_name_user);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_descript);
            btnEdit = (Button)itemView.findViewById(R.id.btn_edit);
            btnDel = (Button)itemView.findViewById(R.id.btn_del);
            tvName.setOnClickListener(this);
            tvDescription.setOnClickListener(this);
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
                    deleteNews(index);
                }
            });
        }

        public void editNews(int index){
            try {
                try {
                    Intent intent = new Intent(context, CreateProjectActivity.class);
                    CoporateNewsObj pb = (CoporateNewsObj) listNews.get(index);
                    intent.putExtra(Config.PROJECT_ID, pb.getId());
                    intent.putExtra(Config.PROJECT_TITLE, pb.getTitle());
                    intent.putExtra(Config.PROJECT_CONTENT, pb.getContent());
                    intent.putExtra(Config.PROJECT_CARID, pb.getCarId());
                    intent.putExtra(Config.PROJECT_FDATE, pb.getFromDate());
                    intent.putExtra(Config.PROJECT_EDATE, pb.getEndDate());
                    intent.putExtra(Config.PROJECT_AVATAR, pb.getUrlAvar());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void deleteNews(int index){
            listNews.remove(index);
            frmContainer.adapter.notifyDataSetChanged();
            // run code xử lý del này ở đây
        }
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                CoporateNewsObj pb = (CoporateNewsObj) listNews.get(index);
                intent.putExtra(Config.Username,dataStoreApp.getUserName());
                intent.putExtra(Config.Project_id,pb.getCarId());
                intent.putExtra(Config.first_name,pb.getFirstname());
                intent.putExtra(Config.status_response,1);
                Log.v("statusaaaa",pb.getStatus()+"");
                intent.putExtra(Config.last_name,pb.getLastname());
                intent.putExtra(Config.PROJECT_PHONE,pb.getPhoneNumber());
                intent.putExtra(Config.PROJECT_TITLE, pb.getTitle());
                intent.putExtra(Config.PROJECT_CONTENT, pb.getContent());
                intent.putExtra(Config.PROJECT_CDATE, pb.getcDate());
                intent.putExtra(Config.PROJECT_FDATE, pb.getFromDate());
                intent.putExtra(Config.PROJECT_EDATE, pb.getEndDate());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
