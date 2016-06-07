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

import java.util.ArrayList;
import java.util.List;

import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.OnLoadMoreListener;
import hao.bk.com.models.User;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;

/**
 * Created by HungChelsea on 06-Jun-16.
 */
public class CommentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<User> mUsers = new ArrayList<>();
    private UserAdapter mUserAdapter;
    Toolbar toolbar;
    MyProgressDialog mpdl;

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_display_message);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);
        initToolBar();
//        mpdl = new MyProgressDialog(getBaseContext());
//        mpdl.showLoading(getString(R.string.txt_loading));
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("LoadMoreRecycleView");

        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setName("Name " + i);
            user.setEmail("alibaba" + i + "@gmail.com");
            mUsers.add(user);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

                        //Load data
//                        int index = mUsers.size();
//                        mUsers.size() = 30;

                        int index = 15;
                        int j = index;
                        int end = index + 5;
                        for (int i = index; i < end; i++) {
                            User user = new User();
                            user.setName("Name " + i);
                            user.setEmail("mkkkkkk" + i + "@gmail.com");
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
        ImageButton btn_comment = (ImageButton) findViewById(R.id.imb_send);
        final EditText edt_comment = (EditText) findViewById(R.id.edt_chat);
        assert btn_comment != null;
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_comment.getText().toString().trim() != null && edt_comment.getText().toString().trim() != "") {
                    User user = new User();
                    user.setName("hungchelsea");
                    user.setEmail(edt_comment.getText().toString().trim());
                    mUsers.add(0, user);
                } else {
                    Toast.makeText(getBaseContext(), "Bạn chưa nhập bình luận", Toast.LENGTH_SHORT).show();
                }

                edt_comment.setText("");
                mUserAdapter.notifyDataSetChanged();
                mUserAdapter.setLoaded();
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
                User user = mUsers.get(position);
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.tvName.setText(user.getName());
                userViewHolder.tvEmailId.setText(user.getEmail());
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
