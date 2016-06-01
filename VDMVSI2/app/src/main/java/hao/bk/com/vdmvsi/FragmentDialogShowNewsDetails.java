package hao.bk.com.vdmvsi;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;

/**
 * Created by T430 on 5/20/2016.
 */
public class FragmentDialogShowNewsDetails extends DialogFragment {

    MainActivity main;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    TextView tvTitle, tvIntros;
    ImageView ivNews, btnBack;
    WebView webView;

    static NewsObj newsObj;

    public static FragmentDialogShowNewsDetails newInstance(NewsObj mp) {
        FragmentDialogShowNewsDetails fragmet = new FragmentDialogShowNewsDetails();
        Bundle args = new Bundle();
        fragmet.setArguments(args);
        newsObj = mp;
        return fragmet;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        main = (MainActivity) context;
        dataStoreApp = new DataStoreApp(context);
        toastUtil = new ToastUtil(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_show_new_details, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        tvTitle = (TextView) v.findViewById(R.id.tv_tile);
        tvIntros = (TextView) v.findViewById(R.id.tv_intros);
        webView = (WebView) v.findViewById(R.id.wv);
        btnBack = (ImageView) v.findViewById(R.id.btn_back);
        ivNews = (ImageView) v.findViewById(R.id.img_news);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showData();
    }

    private void showData() {
        try {
            Picasso.with(main).load(newsObj.getSetUrlNew()).resize(120, 60).into(ivNews);
        }catch (Exception e){
            e.printStackTrace();
        }
        tvTitle.setText(newsObj.getTitle());
        tvIntros.setText(newsObj.getIntros());
        webView.loadData(newsObj.getContent(), "text/html; charset=utf-8", "utf-8");
    }
}