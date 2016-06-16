package hao.bk.com.chat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.Comment;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.LCareObj;
import hao.bk.com.models.OnLoadMoreListener;
import hao.bk.com.models.SupportObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HungChelsea on 10-Jun-16.
 */
public class CreateSupportActivity extends AppCompatActivity {

    EditText edtTitle,edtContent;
    Button btnCreateNew;
    private UserAdapter mUserAdapter;
    private ArrayList<Comment> mUsers = new ArrayList<>();
    private static ArrayList<Comment> listNews = new ArrayList<>();
    ImageButton btn_comment;
    EditText edt_comment;
    private RecyclerView mRecyclerView;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    ViewToolBar vToolBar;
    View viewRoot;
    SupportObj myObj = null;
    ArrayList<LCareObj> listCareObjs;
    AppCompatSpinner spnCare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_support);
        listCareObjs = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myObj = new SupportObj();
            myObj.setId(extras.getInt(Config.Project_id,0));
            myObj.setUsername(extras.getString(Config.Username,""));
            myObj.setContent(extras.getString(Config.PROJECT_CONTENT, ""));
            myObj.setTitle(extras.getString(Config.PROJECT_TITLE,""));
        }
        initViews();
    }


    public void initViews() {
        edtTitle = (EditText) findViewById(R.id.edt_title_support);
        edtContent = (EditText) findViewById(R.id.edt_txt_content_support);
        btnCreateNew = (Button) findViewById(R.id.btn_create_support);
        btn_comment = (ImageButton) findViewById(R.id.imb_send_comment);
        edt_comment = (EditText) findViewById(R.id.edt_chat_comment);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleView);
        if (myObj != null) {
            btnCreateNew.setVisibility(View.INVISIBLE);
            edtTitle.setVisibility(View.INVISIBLE);
            showInfo();
        }else{
            mRecyclerView.setVisibility(View.INVISIBLE);
            btn_comment.setVisibility(View.INVISIBLE);
            edt_comment.setVisibility(View.INVISIBLE);
        }
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                Map users = new HashMap();
                createNewSupport(users);
            }
        });
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
//        runGetListCare();

    }

    private void showInfo() {
        if (myObj != null) {
//            vToolBar.showTextTitle(myObj.getTitle());
            edtTitle.setText(Html.fromHtml(getString(R.string.txt_tile_project) + "<br><font color='black'>" + myObj.getTitle() + "</font>"));
            edtContent.setText(Html.fromHtml(getString(R.string.txt_content) + "<br><font color='black'>" + myObj.getContent() + "</font>"));
            edtContent.setEnabled(false);
            runGetNews();
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_comment.getText().toString().trim() != null && !edt_comment.getText().toString().trim().equals("")) {
                        Comment user = new Comment();
                        user.setUsername(myObj.getUsername());
                        user.setContent(edt_comment.getText().toString().trim());
                        mUsers.add(0, user);
                        Map users = new HashMap();
                        if (myObj != null) {
                            users.put("username", myObj.getUsername());
                            users.put("project_id", myObj.getId() + "");
                            users.put("content", edt_comment.getText().toString().trim());
                        }
                        Log.v("username", users.get("content") + "");
                        addComment(users, "getCommentSupport");
                    } else {
                        Toast.makeText(getBaseContext(), "Bạn chưa nhập bình luận", Toast.LENGTH_SHORT).show();
                    }

                    edt_comment.setText("");
                    mUserAdapter.notifyDataSetChanged();
                    mUserAdapter.setLoaded();
                }
            });
        }
    }


    public boolean validate() {
        if (TextUtils.isEmpty(edtContent.getText().toString())) {
            edtContent.setError(getString(R.string.txt_content));
            return false;
        }
        return true;
    }

    public void createNewSupport(Map users) {
        if (!validate())
            return;
        if (!UtilNetwork.checkInternet(this, getString(R.string.check_internet))) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        users.put("txt_username", dataStoreApp.getUserName());
        users.put("txt_carid", "0");
        users.put("txt_title",edtTitle.getText().toString());
        users.put("txt_content", edtContent.getText().toString());
        Call<JsonObject> call = serverNetWorkAPI.addNewSupport(users);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Util.LOGD("20_5", response.body().toString());

                try {
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if (!status) {
                        toastUtil.showToast(getString(R.string.txt_error_common));
                        return;
                    } else {
                        addMyProjectSuccess();
                        return;
                    }
                } catch (Exception e) {
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                toastUtil.showToast(getString(R.string.txt_error_common));
                return;
            }
        });
    }

    public void addMyProjectSuccess() {
        toastUtil.showToast(getString(myObj == null ? R.string.txt_success_add_support : R.string.txt_success_edit_project));
        this.onBackPressed();
    }

    public void runGetNews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey", Config.PUBLIC_KEY);
        hashMap.put("action", "getCommentSupport");
        hashMap.put("support_id", myObj.getId() + "");
        Call<JsonObject> call = stackOverflowAPI.getCommentProject(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                mpdl.hideLoading();
                try {
                    if (response.body().has(Config.status_response)) {

                        boolean status = response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
//                            notifyDataSetChanged();
                            return;
                        }
                    }
                } catch (Exception e) {
                    toastUtil.showToast(getString(R.string.txt_error_common));
//                    notifyDataSetChanged();
                    return;
                }
                try {
                    listNews.clear();
                    listNews.addAll(JsonCommon.getComment(response.body().getAsJsonArray("data")));
                    CreateSupportActivity.this.runOnUiThread(new Runnable() {
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

                            mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mUserAdapter = new UserAdapter();
                            mRecyclerView.setAdapter(mUserAdapter);

                            mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    mUsers.add(null);
                                    mUserAdapter.notifyItemInserted(mUsers.size() - 1);

                                    //Load more data for reyclerview
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

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
                }
//                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                mpdl.hideLoading();
//                notifyDataSetChanged();
            }
        });
    }

    public void addComment(Map comment, String action) {
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
                View view = LayoutInflater.from(CreateSupportActivity.this).inflate(R.layout.layout_user_item, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(CreateSupportActivity.this).inflate(R.layout.layout_loading_item, parent, false);
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
