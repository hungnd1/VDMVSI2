package hao.bk.com.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.ProductObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/22/2016.
 */
public class ProductDetailActivity extends AppCompatActivity {

    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    static ProductObj productObj;
    TextView tvTitle, tvContent, tvCdate, tvFromDate, tvEndate;
    ViewToolBar vToolBar;
    View viewRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        Bundle extras = getIntent().getExtras();
        productObj = new ProductObj();
        if (extras != null) {
            productObj.setName(extras.getString(Config.PRODUCT_NAME, ""));
            productObj.setCompany(extras.getString(Config.PRODUCT_COMPANY, ""));
            productObj.setUrlThumnails(extras.getString(Config.PRODUCT_THUMB, ""));
        }
        initViews();
    }

    public void initViews(){
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
        vToolBar.showTextTitle(productObj.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
