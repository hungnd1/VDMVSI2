package hao.bk.com.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.NewsVsiObj;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/22/2016.
 */
public class NewsDetailActivity extends AppCompatActivity {

    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    TextView tvIntros,tvTitle,tvDate;
    ImageView ivNews;
    WebView webView;
    ViewToolBar vToolBar;
    View viewRoot;
    NewsObj newsObj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        Bundle extras = getIntent().getExtras();
        newsObj = new NewsObj();
        if (extras != null) {
            newsObj.setTitle(extras.getString(Config.NEWS_TITLE, ""));
            newsObj.setIntros(extras.getString(Config.NEWS_INTRO, ""));
            newsObj.setContent(extras.getString(Config.NEWS_CONTENT, ""));
            newsObj.setSetUrlNew(extras.getString(Config.NEWS_IMG, ""));
            newsObj.setTime(extras.getString(Config.NEWS_TIME, ""));
        }
        initViews();
    }

    public void initViews() {
        tvIntros = (TextView) findViewById(R.id.tv_intros);
        tvTitle = (TextView) findViewById(R.id.tv_title2);
        tvDate = (TextView) findViewById(R.id.tv_date);
        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setBuiltInZoomControls(true);
        ivNews = (ImageView) findViewById(R.id.img_news);
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
        showData();
    }

    private void showData() {
        try {
            Picasso.with(this).load(newsObj.getSetUrlNew()).into(ivNews);
        }catch (Exception e){
        }
        tvIntros.setText(newsObj.getIntros());
        tvTitle.setText(newsObj.getTitle());
        tvDate.setText((newsObj).getTime());
        Log.d("title",newsObj.getTitle());
        Log.d("title2",tvTitle.getText().toString());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(13);
        webView.loadData("<style>img{display: inline; height: auto; max-width: 100%;}</style>"+newsObj.getContent(), "text/html; charset=utf-8", "utf-8");
        Log.d("title3",tvTitle.getText().toString());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
