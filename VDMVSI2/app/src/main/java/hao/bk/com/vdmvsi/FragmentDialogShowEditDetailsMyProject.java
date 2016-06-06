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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.LCareObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 5/20/2016.
 */
public class FragmentDialogShowEditDetailsMyProject extends DialogFragment {

    MainActivity main;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    ArrayList<LCareObj> listCareObjs;
    static CoporateNewsObj myProjectObj;
    ImageView btnBack;
    EditText tvTitle, tvContent;
    MyProgressDialog mpdl;
    TextView tvCdate, tvFromDate, tvEndate;
    AppCompatSpinner spnCare;
    int careIdPus;
    String myCarePush;
    public static FragmentDialogShowEditDetailsMyProject newInstance(NewsObj mp) {
        FragmentDialogShowEditDetailsMyProject fragmet = new FragmentDialogShowEditDetailsMyProject();
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
        mpdl = new MyProgressDialog(context);
        toastUtil = new ToastUtil(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCareObjs = new ArrayList<>();
    }

    public void setDataSpinnerCar(final ArrayList<LCareObj> listCareObjs){
        ArrayList<String> list = new ArrayList<>();
        for(LCareObj obj : listCareObjs){
            list.add(obj.getName());
            Util.LOGD("20-5: " + obj.getName(), obj.getId()+"");
        }
        ArrayAdapter<String> adp= new ArrayAdapter<String>(main,
                android.R.layout.simple_list_item_1,list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCare.setAdapter(adp);

        spnCare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int pos, long arg3) {
                //---save the values in the EditText view to preferences---
                myCarePush = spnCare.getSelectedItem().toString();
                for (LCareObj obj : listCareObjs) {
                    if (obj.getName().equals(myCarePush)) {
                        careIdPus = obj.getId();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(main.getWindow().getDecorView().getWidth(), main.getWindow().getDecorView().getHeight() - 200);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_show_edit_details_myproject, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        spnCare = (AppCompatSpinner) v.findViewById(R.id.spn_major);
        tvTitle = (EditText) v.findViewById(R.id.edt_title_project_edit);
        tvContent = (EditText) v.findViewById(R.id.edt_content_edit);
        tvFromDate = (TextView) v.findViewById(R.id.tv_start_date_edit);
        tvEndate = (TextView) v.findViewById(R.id.tv_end_date_edit);
        btnBack = (ImageView) v.findViewById(R.id.btn_back_edit);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showData();
        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                getTimePicker(tvFromDate);
            }
        });
        tvEndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                getTimePicker(tvEndate);

            }
        });
        Button btnCreateNew = (Button) v.findViewById(R.id.btn_create_new_edit);
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                updateNewProject();
            }
        });
        runGetListCare();

    }

    public boolean validate(){
        if(TextUtils.isEmpty(tvTitle.getText().toString())){
            tvTitle.setError(getString(R.string.txt_tile_project));
            return false;
        }
        if(getString(R.string.txt_start_date_pro).equals(tvFromDate.getText().toString())){
            toastUtil.showToast(getString(R.string.txt_start_date_pro));
            return false;
        }
        if(getString(R.string.txt_end_date_pro).equals(tvEndate.getText().toString())){
            toastUtil.showToast(getString(R.string.txt_end_date_pro));
            return false;
        }
        if(TextUtils.isEmpty(tvContent.getText().toString())){
            tvContent.setError(getString(R.string.txt_content));
            return false;
        }
        return true;
    }

    public void getTimePicker(final TextView tv) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                tv.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };
        new DatePickerDialog(main, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showData() {
        tvTitle.setText(myProjectObj.getTitle());
        tvContent.setText(myProjectObj.getContent());
        tvFromDate.setText(Html.fromHtml(getString(R.string.txt_start_date_pro) + HViewUtils.getTimeViaMiliseconds(myProjectObj.getFromDate())));
        tvEndate.setText(Html.fromHtml(getString(R.string.txt_end_date_pro) + HViewUtils.getTimeViaMiliseconds(myProjectObj.getEndDate())));
    }
    public void updateNewProject(){
        if(!validate())
            return;
        NewProjectObj obj = new NewProjectObj();
        obj.setTitle(tvTitle.getText().toString());
        obj.setCare(myCarePush);
        obj.setFromDate(tvFromDate.getText().toString());
        obj.setEndDate(tvEndate.getText().toString());
        obj.setContent(tvContent.getText().toString());
        if(!UtilNetwork.checkInternet(main, getString(R.string.check_internet))){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        mpdl.showLoading("");
        Map users = new HashMap();
        users.put("txt_id",myProjectObj.getId());
        users.put(" txt_username", dataStoreApp.getUserName());
        users.put("txt_carid", careIdPus+"");
        users.put("txt_title", obj.getTitle());
        users.put("txt_content", obj.getContent());
        Util.LOGD("20_5", careIdPus + "");
        users.put("txt_fdate", obj.getFromDate());
        users.put("txt_edate", obj.getEndDate());
        Call<JsonObject> call = serverNetWorkAPI.addNewProject(users);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>(){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Util.LOGD("20_5", response.body().toString());
                try{
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if(!status){
                        toastUtil.showToast(getString(R.string.txt_error_common));
                        return;
                    } else {
                        addMyProjectSuccess();
                        return;
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mpdl.hideLoading();
                Util.LOGD("20_5_a", t.toString());
                // toastUtil.showToast(getString(R.string.txt_error_common));
                addMyProjectSuccess();
            }
        });
    }
    // get chuyen nganh
    public void runGetListCare(){
        if(!UtilNetwork.checkInternet(main,getString(R.string.txt_check_internet))){
            toastUtil.showToast(getString(R.string.txt_check_internet));
            return;
        }
        mpdl.showLoading("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getListCare);
        hashMap.put("username", dataStoreApp.getUserName());

        Call<JsonObject> call = stackOverflowAPI.getNews(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mpdl.hideLoading();
                try{
                    if(response.body().has(Config.status_response)){
                        boolean status =response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
                            toastUtil.showToast(getString(R.string.txt_error_common));
                            return;
                        }
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
                try {
                    listCareObjs.clear();
                    listCareObjs.addAll(JsonCommon.getListCare(response.body().getAsJsonArray("data")));
                    setDataSpinnerCar(listCareObjs);
                }catch (Exception e){
                }
                if(listCareObjs.size() == 0){
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
                mpdl.hideLoading();
                if(listCareObjs.size() == 0){
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }
        });
    }

    public void addMyProjectSuccess(){
        toastUtil.showToast(getString(R.string.txt_success_add_project));
        dismiss();
    }
    private class NewProjectObj{
        private String title;
        private String care;
        private String fromDate;
        private String endDate;
        private String content;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setCare(String care) {
            this.care = care;
        }

        public String getCare() {
            return care;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return title + "\n" + care + "\n" + fromDate + "\n" + endDate + "\n" + content;
        }
    }

}

