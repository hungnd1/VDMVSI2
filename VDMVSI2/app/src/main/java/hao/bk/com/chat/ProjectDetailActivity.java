package hao.bk.com.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/22/2016.
 */
public class ProjectDetailActivity extends AppCompatActivity {

    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    static CoporateNewsObj myProjectObj;
    TextView tvTitle, tvContent, tvCdate, tvFromDate, tvEndate;
    ViewToolBar vToolBar;
    View viewRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        Bundle extras = getIntent().getExtras();
        myProjectObj = new CoporateNewsObj();
        if (extras != null) {
            myProjectObj.setTitle(extras.getString(Config.PROJECT_TITLE, ""));
            myProjectObj.setContent(extras.getString(Config.PROJECT_CONTENT, ""));
            myProjectObj.setcDate(extras.getLong(Config.PROJECT_CDATE, 0));
            myProjectObj.setFromDate(extras.getLong(Config.PROJECT_FDATE, 0));
            myProjectObj.setEndDate(extras.getLong(Config.PROJECT_EDATE, 0));
        }
        initViews();
    }

    public void initViews(){
//        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvFromDate = (TextView) findViewById(R.id.tv_from_date);
        tvEndate = (TextView) findViewById(R.id.tv_e_date);
        tvCdate = (TextView) findViewById(R.id.tv_c_date);
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
        showData();
    }
    private void showData(){
        vToolBar.showTextTitle(myProjectObj.getTitle());
        //tvTitle.setText(Html.fromHtml(getString(R.string.txt_tile_project) + "<br><font color='black'>"+myProjectObj.getTitle()+"</font>"));
        tvContent.setText(Html.fromHtml(getString(R.string.txt_content) + "<br><font color='black'>"+myProjectObj.getContent()+"</font>"));
        tvCdate.setText(Html.fromHtml(getString(R.string.txt_c_date_pro) + "<br><font color='black'>"+ HViewUtils.getTimeViaMiliseconds(myProjectObj.getcDate())+"</font>"));
        tvFromDate.setText(Html.fromHtml(getString(R.string.txt_start_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getFromDate())+"</font>"));
        tvEndate.setText(Html.fromHtml(getString(R.string.txt_end_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getEndDate())+"</font>"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
