package hao.bk.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.FragmentCoporateNew;
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class InterestingNewsAdapter  extends RecyclerView.Adapter<InterestingNewsAdapter.ViewHolder>  {

    static ArrayList<NewsObj> listNews;
    public  FragmentCoporateNew frmContainer;
    public  Context context;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    public InterestingNewsAdapter(FragmentCoporateNew frmContainer, ArrayList<NewsObj> listNews){
        this.listNews = listNews;
        this.frmContainer = frmContainer;
        this.context = frmContainer.getContext();
        dataStoreApp = new DataStoreApp(context);
        toastUtil = frmContainer.toastUtil;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_news_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageNews.setImageResource(R.drawable.ic_avatar);
        CoporateNewsObj obj = (CoporateNewsObj) listNews.get(position);
        holder.tvName.setText(obj.getNameUser()+" > "+obj.getTitle());
        holder.tvTime.setText(HViewUtils.getTimeViaMiliseconds(obj.getcDate()));
        holder.index = position;
        holder.tvTitle.setVisibility(View.GONE);
        holder.tvDescription.setText(obj.getContent());
    }

    @Override
    public int getItemCount() {
            return listNews != null ? listNews.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tvTitle;
        TextView tvName;
        TextView tvTime;
        TextView tvDescription;
        int index;
        CircleImageView imageNews;
        Button btnLike, btnComment, btnCall;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_news);
            imageNews = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvName = (TextView)itemView.findViewById(R.id.tv_name_user);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_descript);
            btnLike = (Button)itemView.findViewById(R.id.btn_like);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    likeNews(index);
                }
            });
            btnComment = (Button)itemView.findViewById(R.id.btn_comment);
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    commentNews(index);
                }
            });
            btnCall = (Button)itemView.findViewById(R.id.btn_call);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(HViewUtils.isFastDoubleClick())
                        return;
                    callOwnerNews(index);
                }
            });
        }
        public void runCareProject(CoporateNewsObj obj, boolean flag){
            if(!UtilNetwork.checkInternet(context,context.getString(R.string.txt_check_internet))){
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL_GET)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // Khởi tạo các cuộc gọi cho Retrofit 2.0
            NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("publicKey",Config.PUBLIC_KEY);
            hashMap.put("action", flag ? Config.careProject :Config.cancelCareProject);
            hashMap.put("project_id", obj.getId()+"");
            hashMap.put("username", dataStoreApp.getUserName());

            Call<JsonObject> call = stackOverflowAPI.runCareProject(hashMap);
            // Cuộc gọi bất đồng bọ (chạy dưới background)
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Util.LOGD("20-5onResponse", response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Util.LOGD("20-5onFailure", t.toString());
                }
            });
        }
        @Override
        public void onClick(View v) {
            // show tin tuc chi tiet
        }
        public void likeNews(int index){
            CoporateNewsObj obj = (CoporateNewsObj)listNews.get(index);
            if(context.getString(R.string.txt_care).equals(btnLike.getText().toString())) {
                btnLike.setText(context.getString(R.string.txt_not_interest));
                btnLike.setTextColor(context.getResources().getColor(R.color.PrimaryDarkColor));
                // run code xử lý quan tâm tin này ở đây
                runCareProject(obj , true);
            } else {
                btnLike.setText(context.getString(R.string.txt_care));
                runCareProject(obj , false);
                btnLike.setTextColor(context.getResources().getColor(R.color.dark_ness_hint));
            }
            // Xử lý like ở đây
        }
        public void commentNews(int index){

            // Xử lý like ở đây
        }
        public void callOwnerNews(int index){
            // Xử lý goi ng dang tin
            CoporateNewsObj obj = (CoporateNewsObj)listNews.get(index);
            if(TextUtils.isEmpty(obj.getPhoneNumber())){
                toastUtil.showToast(context.getString(R.string.txt_not_phone_owner));
            } else {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
			    //Intent callIntent = new Intent(Intent.ACTION_CALL);c
                callIntent.setData(Uri.parse("tel:" + obj.getPhoneNumber()));
                context.startActivity(callIntent);
            }
        }
    }


}
