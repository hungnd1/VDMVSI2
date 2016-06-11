package hao.bk.com.chat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.LCareObj;
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

    EditText edtContent;
    Button btnCreateNew;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    ViewToolBar vToolBar;
    View viewRoot;
    CoporateNewsObj myObj = null;
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
            myObj = new CoporateNewsObj();
            myObj.setContent(extras.getString(Config.PROJECT_CONTENT, ""));
        }
        initViews();
    }

    public void runGetListCare() {
        if (!UtilNetwork.checkInternet(this, getString(R.string.txt_check_internet))) {
            toastUtil.showToast(getString(R.string.txt_check_internet));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey", Config.PUBLIC_KEY);
        hashMap.put("action", Config.getListCare);
        hashMap.put("username", dataStoreApp.getUserName());

        Call<JsonObject> call = stackOverflowAPI.getNews(hashMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body().has(Config.status_response)) {
                        boolean status = response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
                            toastUtil.showToast(getString(R.string.txt_error_common));
                            return;
                        }
                    }
                } catch (Exception e) {
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
                try {
                    listCareObjs.clear();
                    listCareObjs.addAll(JsonCommon.getListCare(response.body().getAsJsonArray("data")));
//                    setDataSpinnerCar();
                } catch (Exception e) {
                }
                if (listCareObjs.size() == 0) {
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
                if (listCareObjs.size() == 0) {
                    toastUtil.showToast(getString(R.string.txt_server_not_data));
                }
            }
        });
    }

    public void initViews() {
        edtContent = (EditText) findViewById(R.id.edt_txt_content_support);
        btnCreateNew = (Button) findViewById(R.id.btn_create_support);
        if (myObj != null) {
            btnCreateNew.setVisibility(View.INVISIBLE);
        }
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                Map users = new HashMap();
                createNewProject(users);
            }
        });
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        vToolBar.showButtonBack(true);
//        runGetListCare();
        showInfo();
    }

    private void showInfo() {
        if (myObj != null) {
            edtContent.setText(myObj.getContent());
        }
    }


    public boolean validate() {
        if (TextUtils.isEmpty(edtContent.getText().toString())) {
            edtContent.setError(getString(R.string.txt_content));
            return false;
        }
        return true;
    }

    public void createNewProject(Map users) {
        Log.v("users1", users.toString());
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
        users.put("txt_carid", ((LCareObj) spnCare.getSelectedItem()).getId() + "");
        users.put("txt_content", edtContent.getText().toString());
        if (myObj != null) users.put("txt_id", myObj.getId() + "");
        Call<JsonObject> call = myObj != null ? serverNetWorkAPI.editProject(users) : serverNetWorkAPI.addNewProject(users);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Util.LOGD("20_5", response.body().toString());
                Log.v("test", response.body().toString());
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
                Log.v("erro_create", t.toString());
                toastUtil.showToast(getString(R.string.txt_error_common));
                return;
            }
        });
    }

    public void addMyProjectSuccess() {
        toastUtil.showToast(getString(myObj == null ? R.string.txt_success_add_project : R.string.txt_success_edit_project));
        this.onBackPressed();
    }
}
