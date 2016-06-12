package hao.bk.com.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
    TextView tvProductName, tvIntro, tvCompany;
    WebView webView;
    ImageView imgProduct;
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
            productObj.setId(extras.getString(Config.PRODUCT_ID, ""));
            productObj.setUsername(extras.getString(Config.PRODUCT_USERNAME, ""));
            productObj.setCompanyId(extras.getString(Config.PRODUCT_COMPANY_ID, ""));
            productObj.setIntro(extras.getString(Config.PRODUCT_INTRO, ""));
            productObj.setFulltext(extras.getString(Config.PRODUCT_FULLTEXT, ""));
            productObj.setPro_code(extras.getString(Config.PRODUCT_CODE, ""));
        }
        initViews();
    }

    public void initViews(){
        tvProductName = (TextView) findViewById(R.id.tv_product_name);
        tvCompany = (TextView) findViewById(R.id.tv_product_company);
        webView = (WebView) findViewById(R.id.wv);
        tvIntro = (TextView) findViewById(R.id.tv_intros);
        imgProduct = (ImageView) findViewById(R.id.img_product);
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
        showData();
    }
    private void showData(){
        tvProductName.setText(productObj.getName());
        tvIntro.setText(Html.fromHtml(productObj.getIntro()));
        tvCompany.setText(productObj.getCompany());
        imgProduct.setImageResource(R.drawable.nom);
        try {
            Picasso.with(this).load(productObj.getUrlThumnails())
                    .placeholder(R.drawable.nom)
                    .into(imgProduct);
        } catch (Exception e) {
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(13);
        webView.loadData("<style>img{display: inline; height: auto; max-width: 100%;}</style>"+productObj.getFulltext(), "text/html; charset=utf-8", "utf-8");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
