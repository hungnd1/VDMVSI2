package hao.bk.com.comment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.Comment;
import hao.bk.com.models.IFilter;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.OnLoadMoreListener;
import hao.bk.com.models.User;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

/**
 * Created by HungChelsea on 06-Jun-16.
 */
public class CommentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter adapter;
    private ArrayList<Comment> mUsers = new ArrayList<>();
    private static ArrayList<Comment> listNews = new ArrayList<>();
    private ArrayList<Comment> listComment;
    private UserAdapter mUserAdapter;
    Toolbar toolbar;
    ToastUtil toastUtil;
    Comment mycomment = null;
    MyProgressDialog mpdl;
    ImageButton btn_comment;
    EditText edt_comment;
    String action = null;

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_display_message);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(CommentActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mycomment = new Comment();
            mycomment.setPar_id(extras.getInt("project_id", 0));
            mycomment.setUsername(extras.getString(Config.Username, ""));
        }
        action = extras.getString(Config.ACTION_COMMENT, "");
        Log.v("comment", mycomment.getPar_id() + "");
//        Toast.makeText(getBaseContext(),extras.getString(Config.Project_id),Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_comment);
        btn_comment = (ImageButton) findViewById(R.id.imb_send_comment);
        edt_comment = (EditText) findViewById(R.id.edt_chat_comment);
        initToolBar();
        runGetNews(action);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_comment.getText().toString().trim() != null && !edt_comment.getText().toString().trim().equals("")) {
                    Comment user = new Comment();
                    user.setUsername(mycomment.getUsername());
                    user.setContent(edt_comment.getText().toString().trim());
                    mUsers.add(0, user);
                    Map users = new HashMap();
                    if (mycomment != null) {
                        users.put("username", mycomment.getUsername());
                        users.put("project_id", mycomment.getPar_id() + "");
                        users.put("content", edt_comment.getText().toString().trim());
                    }
                    Log.v("username", users.get("content") + "");
                    addComment(users,action);
                } else {
                    Toast.makeText(getBaseContext(), "Bạn chưa nhập bình luận", Toast.LENGTH_SHORT).show();
                }

                edt_comment.setText("");
                mUserAdapter.notifyDataSetChanged();
                mUserAdapter.setLoaded();
            }
        });

    }


    public void runGetNews(String id) {
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
        hashMap.put("action", id);
        if(action.equals("getCommentSupport")){
            hashMap.put("support_id", mycomment.getPar_id() + "");
        }else{
            hashMap.put("project_id", mycomment.getPar_id() + "");
        }
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
                    CommentActivity.this.runOnUiThread(new Runnable() {
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

    public void addComment(Map comment,String action) {
        Log.v("logcomment", "1");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        Call<JsonObject> call = action.equals("getCommentSupport")? serverNetWorkAPI.addCommentSupport(comment): serverNetWorkAPI.addComment(comment);
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
                View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.layout_user_item, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.layout_loading_item, parent, false);
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
