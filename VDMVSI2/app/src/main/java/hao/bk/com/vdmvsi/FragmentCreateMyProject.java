package hao.bk.com.vdmvsi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

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
import hao.bk.com.models.MyProjectObj;
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
 * Created by Haodv
 */
public class FragmentCreateMyProject extends DialogFragment {

    MainActivity main;
    EditText edtTitleProject, edtContent;
    Button btnCreateNew;
    TextView btnStartDate, btnEndate;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    static CoporateNewsObj myObj = null;
    ArrayList<LCareObj> listCareObjs;
    AppCompatSpinner spnCare;
    ImageView btnBack;

    public static FragmentCreateMyProject newInstance() {
        FragmentCreateMyProject fragmentCreateMyProject = new FragmentCreateMyProject();
        Bundle args = new Bundle();
        fragmentCreateMyProject.setArguments(args);
        return fragmentCreateMyProject;
    }

    public static FragmentCreateMyProject newInstance(NewsObj mp) {
        FragmentCreateMyProject fragmentCreateMyProject = new FragmentCreateMyProject();
        Bundle args = new Bundle();
        fragmentCreateMyProject.setArguments(args);
        myObj = (CoporateNewsObj) mp;
        return fragmentCreateMyProject;
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
        listCareObjs = new ArrayList<>();
    }
    public void getTimePicker(final TextView tv){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                tv.setText(dayOfMonth+"/"+monthOfYear+"/" + year);
            }
        };
        new DatePickerDialog(main, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_create_myproject, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(v);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(main.getWindow().getDecorView().getWidth(),main.getWindow().getDecorView().getHeight()-200);
    }
    public void runGetListCare(){
        if(!UtilNetwork.checkInternet(main,getString(R.string.txt_check_internet))){
            toastUtil.showToast(getString(R.string.txt_check_internet));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getListCare);
        hashMap.put("username", dataStoreApp.getUserName());

        Call<JsonObject> call = stackOverflowAPI.getNews(hashMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                    setDataSpinnerCar();
                }catch (Exception e){
                }
                if(listCareObjs.size() == 0){
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " +t);
                if(listCareObjs.size() == 0){
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }
        });
    }

    public void setDataSpinnerCar(){
        ArrayAdapter<LCareObj> adp= new ArrayAdapter<LCareObj>(main,
                android.R.layout.simple_list_item_1,listCareObjs);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCare.setAdapter(adp);
    }

    public void initViews(View v){
        spnCare = (AppCompatSpinner)v.findViewById(R.id.spn_major);
        edtTitleProject = (EditText)v.findViewById(R.id.edt_title_project);
        edtContent = (EditText)v.findViewById(R.id.edt_content);
        btnStartDate = (TextView)v.findViewById(R.id.tv_start_date);
        btnBack = (ImageView)v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                getTimePicker(btnStartDate);
            }
        });
        btnEndate = (TextView)v.findViewById(R.id.tv_end_date);
        btnEndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                getTimePicker(btnEndate);
            }
        });
        btnCreateNew = (Button)v.findViewById(R.id.btn_create_new);
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                Map users = new HashMap();
                if (myObj != null){
                    users.put("txt_id",myObj.getId());
                }
                createNewProject(users);
            }
        });
        runGetListCare();
        showInfo();
    }

    private void showInfo() {
        if (myObj != null){
            btnCreateNew.setText(getString(R.string.txt_edit_proj));
            edtTitleProject.setText(myObj.getTitle());
            edtContent.setText(myObj.getContent());
            btnStartDate.setText(HViewUtils.getTimeViaMiliseconds(myObj.getFromDate()));
            btnEndate.setText(HViewUtils.getTimeViaMiliseconds(myObj.getEndDate()));
            for (int i=0;i < listCareObjs.size();i++){
                if (listCareObjs.get(i).getId() ==  myObj.getCarId()){
                    spnCare.setSelection(i);
                    return;
                }
            }
        }
    }


    public boolean validate(){
        if(TextUtils.isEmpty(edtTitleProject.getText().toString())){
            edtTitleProject.setError(getString(R.string.txt_tile_project));
            return false;
        }
        if(getString(R.string.txt_start_date_pro).equals(btnStartDate.getText().toString())){
            toastUtil.showToast(getString(R.string.txt_start_date_pro));
            return false;
        }
        if(getString(R.string.txt_end_date_pro).equals(btnEndate.getText().toString())){
            toastUtil.showToast(getString(R.string.txt_end_date_pro));
            return false;
        }
        if(TextUtils.isEmpty(edtContent.getText().toString())){
            edtContent.setError(getString(R.string.txt_content));
            return false;
        }
        return true;
    }

    public void createNewProject(Map users){
        if(!validate())
            return;
        if(!UtilNetwork.checkInternet(main, getString(R.string.check_internet))){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        users.put("txt_username", dataStoreApp.getUserName());
        users.put("txt_carid", ((LCareObj) spnCare.getSelectedItem()).getId());
        users.put("txt_title", edtTitleProject.getText().toString());
        users.put("txt_content", edtContent.getText().toString());
        users.put("txt_fdate", btnStartDate.getText().toString());
        users.put("txt_edate", btnEndate.getText().toString());
        Call<JsonObject> call = serverNetWorkAPI.addNewProject(users);
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
                toastUtil.showToast(getString(R.string.txt_error_common));
                return;
            }
        });
    }
    public void addMyProjectSuccess(){
        toastUtil.showToast(getString(R.string.txt_success_add_project));
        dismiss();
    }

}

