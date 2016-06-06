package hao.bk.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.FragmentCoporateNew;
import hao.bk.com.vdmvsi.FragmentCreateMyProject;
import hao.bk.com.vdmvsi.FragmentDialogShowDetailsMyProject;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class MyProjectNewsAdapter extends  RecyclerView.Adapter<MyProjectNewsAdapter.ViewHolder>  {
    static ArrayList<NewsObj> listNews;
    public FragmentCoporateNew frmContainer;
    public Context context;
    ToastUtil toastUtil;
    public MyProjectNewsAdapter(FragmentCoporateNew frmContainer, ArrayList<NewsObj> listNews){
        this.listNews = listNews;
        this.frmContainer = frmContainer;
        context = frmContainer.getContext();
        toastUtil = frmContainer.toastUtil;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageNews.setImageResource(R.drawable.ic_avatar);
        CoporateNewsObj obj = (CoporateNewsObj) listNews.get(position);
        holder.tvName.setText(obj.getNameUser()+" > "+ obj.getTitle());
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

    public class ViewHolder extends RecyclerView.ViewHolder{
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
                    deleteNews(index);
                }
            });
        }

        public void showNews(int index){
            NewsObj pb = listNews.get(index);
            // run code xử lý show news details here
            FragmentDialogShowDetailsMyProject fragmentCreateMyProject =  FragmentDialogShowDetailsMyProject.newInstance(pb);
            fragmentCreateMyProject.show(frmContainer.getActivity().getFragmentManager(), "");
        }
        public void editNews(int index){
            NewsObj pb = listNews.get(index);

            FragmentCreateMyProject fragmentCreateMyProject =  FragmentCreateMyProject.newInstance(pb);
            fragmentCreateMyProject.show(frmContainer.getActivity().getFragmentManager(), "");
        }
        public void deleteNews(int index){
            listNews.remove(index);
            frmContainer.adapter.notifyDataSetChanged();
            // run code xử lý del này ở đây
        }

    }
}
