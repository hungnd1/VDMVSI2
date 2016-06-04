package hao.bk.com.vdmvsi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.MyProjectObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;

/**
 * Created by T430 on 5/20/2016.
 */
public class FragmentDialogShowDetailsMyProject extends DialogFragment {

    MainActivity main;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    static CoporateNewsObj myProjectObj;
    Button btnBack;
    TextView tvTitle, tvContent, tvCdate, tvFromDate, tvEndate;

    public static FragmentDialogShowDetailsMyProject newInstance(NewsObj mp) {
        FragmentDialogShowDetailsMyProject fragmet = new FragmentDialogShowDetailsMyProject();
        Bundle args = new Bundle();
        fragmet.setArguments(args);
        myProjectObj = (CoporateNewsObj) mp;
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

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(main.getWindow().getDecorView().getWidth(),main.getWindow().getDecorView().getHeight()-200);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_show_details_myproject, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(v);
        return v;
    }

    public void initViews(View v){
        tvTitle = (TextView)v.findViewById(R.id.tv_title);
        tvContent = (TextView)v.findViewById(R.id.tv_content);
        tvFromDate = (TextView)v.findViewById(R.id.tv_from_date);
        tvEndate = (TextView)v.findViewById(R.id.tv_e_date);
        tvCdate = (TextView)v.findViewById(R.id.tv_c_date);
        btnBack = (Button) v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showData();
    }
    private void showData(){
        tvTitle.setText(Html.fromHtml(getString(R.string.txt_tile_project) + "<br><font color='black'>"+myProjectObj.getTitle()+"</font>"));
        tvContent.setText(Html.fromHtml(getString(R.string.txt_content) + "<br><font color='black'>"+myProjectObj.getContent()+"</font>"));
        tvCdate.setText(Html.fromHtml(getString(R.string.txt_c_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getcDate())+"</font>"));
        tvFromDate.setText(Html.fromHtml(getString(R.string.txt_start_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getFromDate())+"</font>"));
        tvEndate.setText(Html.fromHtml(getString(R.string.txt_end_date_pro) + "<br><font color='black'>"+HViewUtils.getTimeViaMiliseconds(myProjectObj.getEndDate())+"</font>"));
    }
}

