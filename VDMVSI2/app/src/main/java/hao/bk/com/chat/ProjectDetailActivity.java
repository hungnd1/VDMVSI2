package hao.bk.com.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.adapter.CircleTransform;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.Comment;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.OnLoadMoreListener;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/22/2016.
 */
public class ProjectDetailActivity extends AppCompatActivity {

    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    private UserAdapter mUserAdapter;
    static CoporateNewsObj myProjectObj;
    TextView tvTitle, tvContent, tvCdate, tvFromDate, tvEndate;
    RelativeLayout lnlChat;
    CircleImageView imageNews;
    ViewToolBar vToolBar;
    View viewRoot;
    private RecyclerView mRecyclerView;
    private ArrayList<Comment> mUsers = new ArrayList<>();
    private static ArrayList<Comment> listNews = new ArrayList<>();
    ImageButton btn_comment;
    EditText edt_comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        Bundle extras = getIntent().getExtras();
        myProjectObj = new CoporateNewsObj();
        if (extras != null) {
            myProjectObj.setNameUser(extras.getString(Config.Username,""));
            myProjectObj.setCarId(extras.getInt(Config.Project_id, 0));
            myProjectObj.setTitle(extras.getString(Config.PROJECT_TITLE, ""));
            myProjectObj.setContent(extras.getString(Config.PROJECT_CONTENT, ""));
            myProjectObj.setcDate(extras.getLong(Config.PROJECT_CDATE, 0));
            myProjectObj.setFromDate(extras.getLong(Config.PROJECT_FDATE, 0));
            myProjectObj.setEndDate(extras.getLong(Config.PROJECT_EDATE, 0));
            myProjectObj.setUrlAvar(extras.getString(Config.PROJECT_AVATAR, ""));
        }
        initViews();
    }

    public void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title2);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btn_comment = (ImageButton) findViewById(R.id.imb_send_comment);
        edt_comment = (EditText) findViewById(R.id.edt_chat_comment);
        imageNews = (CircleImageView) findViewById(R.id.imv_profile);
//        tvFromDate = (TextView) findViewById(R.id.tv_from_date);
//        tvEndate = (TextView) findViewById(R.id.tv_e_date);
        tvCdate = (TextView) findViewById(R.id.tv_date);
        lnlChat = (RelativeLayout) findViewById(R.id.lnl_input_chat);
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
        showData();
        runGetNews();
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_comment.getText().toString().trim() != null && !edt_comment.getText().toString().trim().equals("")) {
                    Comment user = new Comment();
                    user.setUsername(myProjectObj.getNameUser());
                    user.setContent(edt_comment.getText().toString().trim());
                    mUsers.add(0, user);
                    Map users = new HashMap();
                    if (myProjectObj != null) {
                        users.put("username", myProjectObj.getNameUser());
                        users.put("project_id", myProjectObj.getCarId() + "");
                        users.put("content", edt_comment.getText().toString().trim());
                    }
                    Log.v("username", users.get("content") + "");
                    addComment(users, "getCommentProject");
                } else {
                    Toast.makeText(getBaseContext(), "Bạn chưa nhập bình luận", Toast.LENGTH_SHORT).show();
                }

                edt_comment.setText("");
                mUserAdapter.notifyDataSetChanged();
                mUserAdapter.setLoaded();
            }
        });
    }

    private void showData() {
//        vToolBar.showTextTitle(myProjectObj.getTitle());
        if (myProjectObj.getUrlAvar() == null || myProjectObj.getUrlAvar() == "") {
            Picasso.with(getApplicationContext()).load(R.drawable.icon_user).transform(new CircleTransform()).into(imageNews);
        } else {
            Picasso.with(getApplicationContext()).load(myProjectObj.getUrlAvar()).transform(new CircleTransform()).into(imageNews);
        }
        tvTitle.setText(Html.fromHtml(myProjectObj.getTitle()));
        tvContent.setText(Html.fromHtml(myProjectObj.getContent()));
        tvCdate.setText(Html.fromHtml("Ngày "+HViewUtils.getTimeViaMiliseconds(myProjectObj.getcDate())));
//        tvFromDate.setText(Html.fromHtml(getString(R.string.txt_start_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getFromDate())+"</font>"));
//        tvEndate.setText(Html.fromHtml(getString(R.string.txt_end_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getEndDate())+"</font>"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void runGetNews() {
//        mpdl.showLoading(getString(R.string.txt_loading));
        Log.v("1a", "1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.v("1a", "2");
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey", Config.PUBLIC_KEY);
        hashMap.put("action", "getCommentProject");
        hashMap.put("project_id", myProjectObj.getCarId() + "");
        Log.v("1a", "3");
        Call<JsonObject> call = stackOverflowAPI.getCommentProject(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        Log.v("1a", call + "");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                mpdl.hideLoading();
                try {
                    if (response.body().has(Config.status_response)) {

                        boolean status = response.body().get(Config.status_response).getAsBoolean();
                        Log.v("call", status + "");
                        if (!status) {
//                            notifyDataSetChanged();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.v("1a", "5");
                    toastUtil.showToast(getString(R.string.txt_error_common));
//                    notifyDataSetChanged();
                    return;
                }
                try {
                    listNews.clear();
                    listNews.addAll(JsonCommon.getComment(response.body().getAsJsonArray("data")));
                    ProjectDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int sizeofComment = 10;
                            if (listNews.size() < 10) {
                                sizeofComment = listNews.size();
                            }
                            for (int i = 0; i < sizeofComment; i++) {
                                Comment comment = new Comment();
                                comment.setUsername(listNews.get(i).getUsername());
                                comment.setContent(listNews.get(i).getContent());
                                mUsers.add(comment);
                            }
                            Log.v("check", mUsers + "");

                            mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mUserAdapter = new UserAdapter();
                            mRecyclerView.setAdapter(mUserAdapter);

                            mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    Log.e("haint", "Load More");
                                    mUsers.add(null);
                                    mUserAdapter.notifyItemInserted(mUsers.size() - 1);

                                    //Load more data for reyclerview
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("haint", "Load More 2");

                                            //Remove loading item
                                            mUsers.remove(mUsers.size() - 1);
                                            mUserAdapter.notifyItemRemoved(mUsers.size());
                                            int index = 11;
                                            int j = index;
                                            int end = listNews.size();
                                            for (int i = index; i < end; i++) {
                                                Comment user = new Comment();
                                                user.setUsername(listNews.get(i).getUsername());
                                                user.setContent(listNews.get(i).getContent());
                                                mUsers.add(user);
                                                j++;
                                            }
                                            if (j != end) {
                                                mUserAdapter.notifyDataSetChanged();
                                                mUserAdapter.setLoaded();
                                            }
                                        }
                                    }, 2000);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.v("1a", "6");
                }
//                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
                Log.v("1a", "7");
//                mpdl.hideLoading();
//                notifyDataSetChanged();
            }
        });
        Log.v("1a", "8");
    }

    public void addComment(Map comment, String action) {
        Log.v("logcomment", "1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        Call<JsonObject> call = action.equals("getCommentSupport") ? serverNetWorkAPI.addCommentSupport(comment) : serverNetWorkAPI.addComment(comment);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Util.LOGD("20_5", response.body().toString());
                Log.v("test", response.body().toString());
                try {
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if (!status) {
//                        toastUtil.showToast(getString(R.string.txt_error_common));
                        return;
                    } else {
                        return;
                    }
                } catch (Exception e) {
//                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("erro_create", t.toString());
//                toastUtil.showToast(getString(R.string.txt_error_common));
                return;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmailId;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);

            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;

        public UserAdapter() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return mUsers.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(ProjectDetailActivity.this).inflate(R.layout.layout_user_item, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(ProjectDetailActivity.this).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof UserViewHolder) {
                Comment user = mUsers.get(position);
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.tvName.setText(user.getUsername());
                userViewHolder.tvEmailId.setText(user.getContent());
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return mUsers == null ? 0 : mUsers.size();
        }

        public void setLoaded() {
            isLoading = false;
        }


    }

}
