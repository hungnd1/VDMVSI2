package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import hao.bk.com.chat.NewsDetailActivity;
import hao.bk.com.config.Config;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.NewsVsiObj;
import hao.bk.com.vdmvsi.FragmentNews;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/23/2016.
 */
public class NewsItemAdapter extends  RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {


    ArrayList<NewsObj> listNews = new ArrayList<>();
    boolean isNewLastest = false;
    public  FragmentNews frmContainer;
    public  Context context;
    public NewsItemAdapter(FragmentNews frmContainer, ArrayList<NewsObj> listChat){
        this.listNews = listChat;
        this.frmContainer = frmContainer;
        this.context = frmContainer.getContext();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder rssNewsViewHolder;
        View view;
        if(isNewLastest) {
            // card view cho lastest news, la phan tu dau tien cua cardview
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_lastest_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        }
        rssNewsViewHolder = new ViewHolder(view);
        return rssNewsViewHolder;
    }

    @Override
    public int getItemCount() {
        if(listNews != null)
            return listNews.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // is new Lastest
        if(position == 0)
            isNewLastest = true;
        else
            isNewLastest = false;
        return position;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsVsiObj obj = (NewsVsiObj) listNews.get(position);
        holder.tvTitle.setText(obj.getTitle());
        holder.tvPubdate.setText(obj.getcDate());
        holder.index = position;

        try {
            Picasso.with(context).load(obj.getSetUrlNew()).into(holder.imageNews);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tvTitle;
        TextView tvPubdate;
        ImageView imageNews;
        int index;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_news);
            cv.setOnClickListener(this);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title_news);
            tvPubdate = (TextView) itemView.findViewById(R.id.tv_pub_date);
            imageNews = (ImageView)itemView.findViewById(R.id.img_news);
        }

        @Override
        public void onClick(View v) {
            // show tin tuc chi tiet
            try {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                NewsObj obj = listNews.get(index);
                intent.putExtra(Config.NEWS_TITLE, obj.getTitle());
                intent.putExtra(Config.NEWS_INTRO, obj.getIntros());
                intent.putExtra(Config.NEWS_CONTENT, obj.getContent());
                intent.putExtra(Config.NEWS_IMG, obj.getSetUrlNew());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
