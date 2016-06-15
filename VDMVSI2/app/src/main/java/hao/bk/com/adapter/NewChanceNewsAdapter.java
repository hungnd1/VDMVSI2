package hao.bk.com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.ProjectDetailActivity;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.NewsFilter;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.IFilter;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.FragmentCoporateNew;
import hao.bk.com.vdmvsi.R;

import java.util.ArrayList;
import java.util.HashMap;

import hao.bk.com.models.NewsObj;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/22/2016.
 */
public class NewChanceNewsAdapter extends RecyclerView.Adapter<NewChanceNewsAdapter.ViewHolder> implements IFilter{
    ArrayList<NewsObj> listNews;
    public FragmentCoporateNew frmContainer;
    NewsFilter filter;
    DataStoreApp dataStoreApp;
    public Context context;
    ToastUtil toastUtil;

    public NewChanceNewsAdapter(FragmentCoporateNew frmContainer, ArrayList<NewsObj> listNews) {
        this.listNews = listNews;
        this.frmContainer = frmContainer;
        context = frmContainer.getContext();
        toastUtil = new ToastUtil(context);
        dataStoreApp = new DataStoreApp(frmContainer.getContext());
    }

    @Override
    public int getItemCount() {
        return listNews != null ? listNews.size() : 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        CoporateNewsObj obj = (CoporateNewsObj) listNews.get(i);
        if (obj.getUrlAvar() == null || obj.getUrlAvar() == "") {
            Picasso.with(context.getApplicationContext()).load(R.drawable.icon_user).transform(new CircleTransform()).into(holder.imageNews);
        } else {
            Picasso.with(context.getApplicationContext()).load(obj.getUrlAvar()).transform(new CircleTransform()).into(holder.imageNews);
        }
        Log.v("status",String.valueOf(obj.getStatus()));
        if(obj.getStatus() == 1){
            holder.btnCare.setText(context.getString(R.string.txt_ingnore_care));
            holder.btnCare.setTextColor(context.getResources().getColor(R.color.PrimaryDarkColor));
        }else{
            holder.btnCare.setText(context.getString(R.string.txt_care));
            holder.btnCare.setTextColor(context.getResources().getColor(R.color.dark_ness_hint));
        }
        holder.tvName.setText(obj.getNameUser()+" > "+ obj.getTitle());
        holder.tvTime.setText("Ngày "+HViewUtils.getTimeViaMiliseconds(obj.getcDate()));
        holder.index = i;
        holder.tvTitle.setVisibility(View.GONE);
        holder.tvDescription.setText(obj.getContent());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_news_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void filter(CharSequence cs) {
        listNews.clear();
        if (filter!=null) listNews.addAll(filter.filter(cs));
        notifyDataSetChanged();
    }

    @Override
    public void updateFilter() {
        this.filter = new NewsFilter(listNews);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView tvTitle;
        TextView tvName;
        TextView tvTime;
        int index;
        TextView tvDescription;
        CircleImageView imageNews;
        Button btnCare, btnComment, btnCall;

        ViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_news);
//            cv.setOnClickListener(this);
            imageNews = (CircleImageView) itemView.findViewById(R.id.imv_profile);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_user);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_descript);
            btnCare = (Button) itemView.findViewById(R.id.btn_like);
            btnCare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCareNews();
                }
            });
            btnComment = (Button) itemView.findViewById(R.id.btn_comment);
            tvName.setOnClickListener(this);
            tvDescription.setOnClickListener(this);
            //hungnd invisiable button comment
            btnComment.setVisibility(View.GONE);
//            btnComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    toastUtil.showToast("Chức năng này sẽ ra mắt trong thời gian tới");
//                }
//            });
            btnCall = (Button) itemView.findViewById(R.id.btn_call);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callOwnerNews(index);
                }
            });
        }

        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                CoporateNewsObj pb = (CoporateNewsObj) listNews.get(index);
                intent.putExtra(Config.Username,dataStoreApp.getUserName());
                intent.putExtra(Config.Project_id,pb.getCarId());
                intent.putExtra(Config.PROJECT_TITLE, pb.getTitle());
                intent.putExtra(Config.PROJECT_CONTENT, pb.getContent());
                intent.putExtra(Config.PROJECT_CDATE, pb.getcDate());
                intent.putExtra(Config.PROJECT_FDATE, pb.getFromDate());
                intent.putExtra(Config.PROJECT_EDATE, pb.getEndDate());
                intent.putExtra(Config.PROJECT_AVATAR, pb.getUrlAvar());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setCareNews() {
            if (context.getString(R.string.txt_care).equals(btnCare.getText().toString())) {
                btnCare.setText(context.getString(R.string.txt_ingnore_care));
                btnCare.setTextColor(context.getResources().getColor(R.color.PrimaryDarkColor));
                // run code xử lý quan tâm tin này ở đây
                runCareProject((CoporateNewsObj) listNews.get(index), true);
            } else {
                btnCare.setText(context.getString(R.string.txt_care));
                btnCare.setTextColor(context.getResources().getColor(R.color.dark_ness_hint));
                runCareProject((CoporateNewsObj) listNews.get(index), false);
            }
        }

        public void delNews(int index) {
            listNews.remove(index);
            frmContainer.adapter.notifyDataSetChanged();
            // run code xử lý Hủy tin này ở đây
        }

        public void runCareProject(CoporateNewsObj obj, boolean flag) {
            if (!UtilNetwork.checkInternet(context, context.getString(R.string.txt_check_internet))) {
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL_GET)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // Khởi tạo các cuộc gọi cho Retrofit 2.0
            NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("publicKey", Config.PUBLIC_KEY);
            hashMap.put("action", flag ? Config.careProject : Config.cancelCareProject);
            hashMap.put("project_id", obj.getId() + "");
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

        public void callOwnerNews(int index) {
            // Xử lý goi ng dang tin
            CoporateNewsObj obj = (CoporateNewsObj) listNews.get(index);
            if (TextUtils.isEmpty(obj.getPhoneNumber())) {
                toastUtil.showToast(context.getString(R.string.txt_not_phone_owner));
            } else {
                final String x = obj.getPhoneNumber();
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(
                        context);
                alBuilder.setMessage(context.getString(R.string.txt_message) + " " + obj.getPhoneNumber() + " " + context.getString(R.string.txt_no) + " ?");
                final AlertDialog.Builder builder = alBuilder.setPositiveButton(R.string.txt_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + x));
                        context.startActivity(callIntent);
                    }
                });
                alBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alBuilder.create();
                alertDialog.show();
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                //Intent callIntent = new Intent(Intent.ACTION_CALL);c
//                callIntent.setData(Uri.parse("tel:" + obj.getPhoneNumber()));
//                context.startActivity(callIntent);
            }
        }
    }

}
