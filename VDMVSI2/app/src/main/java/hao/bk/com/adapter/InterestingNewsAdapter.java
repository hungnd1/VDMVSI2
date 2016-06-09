package hao.bk.com.adapter;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pubnub.api.PubnubError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.chat.CreateProjectActivity;
import hao.bk.com.chat.ProjectDetailActivity;
import hao.bk.com.comment.CommentActivity;
import hao.bk.com.common.ChatFilter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.NewsFilter;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.IFilter;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.FragmentCoporateNew;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class InterestingNewsAdapter  extends RecyclerView.Adapter<InterestingNewsAdapter.ViewHolder> implements IFilter{

    static ArrayList<NewsObj> listNews;
    public  FragmentCoporateNew frmContainer;
    public  Context context;
    NewsFilter filter;
    public static final int notifyID = 9002;
    NotificationCompat.Builder builder;
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
        CoporateNewsObj obj = (CoporateNewsObj) listNews.get(position);
        if (obj.getUrlAvar() == null || obj.getUrlAvar() == "") {
            Picasso.with(context.getApplicationContext()).load(R.drawable.icon_user).transform(new CircleTransform()).into(holder.imageNews);
        } else {
            Picasso.with(context.getApplicationContext()).load(obj.getUrlAvar()).transform(new CircleTransform()).into(holder.imageNews);
        }
        if(obj.getStatus() == 1){
            holder.btnLike.setText(context.getString(R.string.txt_ingnore_care));
            holder.btnLike.setTextColor(context.getResources().getColor(R.color.PrimaryDarkColor));
        }else{
            holder.btnLike.setText(context.getString(R.string.txt_care));
            holder.btnLike.setTextColor(context.getResources().getColor(R.color.dark_ness_hint));
        }
        holder.tvName.setText(obj.getNameUser()+" > "+obj.getTitle());
        holder.tvTime.setText("Ngày: "+HViewUtils.getTimeViaMiliseconds(obj.getcDate()));
        holder.index = position;
        holder.tvTitle.setVisibility(View.GONE);
        holder.tvDescription.setText(obj.getContent());
    }

    @Override
    public int getItemCount() {
            return listNews != null ? listNews.size() : 0;
    }

    @Override
    public void filter(CharSequence cs) {
        listNews.clear();
        if (filter != null) listNews.addAll(filter.filter(cs));
        notifyDataSetChanged();
    }

    @Override
    public void updateFilter() {
        this.filter = new NewsFilter(listNews);
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
//            cv = (CardView)itemView.findViewById(R.id.cv_news);
//            cv.setOnClickListener(this);
            imageNews = (CircleImageView)itemView.findViewById(R.id.imv_profile);
            tvName = (TextView)itemView.findViewById(R.id.tv_name_user);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_descript);
            btnLike = (Button)itemView.findViewById(R.id.btn_like);
            tvName.setOnClickListener(this);
            tvDescription.setOnClickListener(this);
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
                    Intent intent = new Intent(context.getApplicationContext(), CommentActivity.class);
                    intent.putExtra("project_id",index);
                    context.startActivity(intent);
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
            hashMap.put("publicKey", Config.PUBLIC_KEY);
            hashMap.put("action", flag ? Config.careProject : Config.cancelCareProject);
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
            try {
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                CoporateNewsObj pb = (CoporateNewsObj) listNews.get(index);
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
        public void likeNews(int index){
            CoporateNewsObj obj = (CoporateNewsObj)listNews.get(index);
            if(context.getString(R.string.txt_care).equals(btnLike.getText().toString())) {
                btnLike.setText(context.getString(R.string.txt_not_interest));
                btnLike.setTextColor(context.getResources().getColor(R.color.PrimaryDarkColor));
                // run code xử lý quan tâm tin này ở đây

                runCareProject(obj, true);
            } else {

                if(HViewUtils.isFastDoubleClick())
                    return;
                deleteNews(index);
            }
            // Xử lý like ở đây
        }
        public void deleteNews(int index){
            CoporateNewsObj obj = (CoporateNewsObj)listNews.get(index);
            runCareProject(obj, false);
            listNews.remove(index);
            frmContainer.adapter.notifyDataSetChanged();
            // run code xử lý del này ở đây
        }

        public void commentNews(int index){

            // Xử lý like ở đây
        }
        public void callOwnerNews(int index){
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

    private void sendNotification(String msg) {

        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("msg", msg);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle(context.getApplicationContext().getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_avatar)
                .setLargeIcon(BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.ic_avatar))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(TextUtils.isEmpty(msg)?"You've received new message.":msg));
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(TextUtils.isEmpty(msg)?"New message":msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }


    //hungnd_notification
    public boolean publish(String message) {
        com.pubnub.api.Callback callback = new com.pubnub.api.Callback() {
            public void successCallback(String channel, Object response) {
                Util.LOGD("26_4 publish successCallback", response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                Util.LOGD("26_4 errorCallback publish", error.toString());
            }
        };
        try {
            MainActivity.pubnub.publish(Config.startChannelName + "hungnd1", createMessageSend(message).toString(), callback);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public JsonObject createMessageSend(String message) {
//        mess:{
//            author:nxtuyen
//            mess:content
//            time:321313
//        }
        JsonObject jsonObject = new JsonObject();
        JsonObject objC = new JsonObject();
        objC.addProperty("from", dataStoreApp.getUserName());
        objC.addProperty("title", message);
        jsonObject.add("content", objC);
        return jsonObject;

    }
}
