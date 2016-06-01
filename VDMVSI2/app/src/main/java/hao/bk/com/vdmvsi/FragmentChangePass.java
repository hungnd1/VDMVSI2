package hao.bk.com.vdmvsi;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/26/2016.
 */
public class FragmentChangePass extends DialogFragment {
    private final String tag = "FragmentChangePass";
    EditText edtOldPass, edtNewPass, edtReNewPass;
    DataStoreApp dataStoreApp;
    ToastUtil toastUtil;
    Button btnOk;
    MyProgressDialog mpdl;
    ImageView btnBack;
    String newPass;
    public static FragmentChangePass newInstance() {
        FragmentChangePass fragmentCreateMyProject = new FragmentChangePass();
        return fragmentCreateMyProject;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        toastUtil = new ToastUtil(activity);
        dataStoreApp = new DataStoreApp(activity);
        mpdl = new MyProgressDialog(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmeent_dialog_change_pass, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        initViews(v);
        return v;
    }
    public void initViews(View v) {
        btnBack = (ImageView)v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        edtOldPass = (EditText) v.findViewById(R.id.edt_old_pass);
        edtNewPass = (EditText) v.findViewById(R.id.edt_new_pass);
        edtReNewPass = (EditText) v.findViewById(R.id.edt_renew_pass);
        btnOk = (Button)v.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                if(validate())
                    runChangePass();

            }
        });
    }
    public void runChangePass(){
        if(!UtilNetwork.checkInternet(getActivity(), getString(R.string.check_internet))){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        mpdl.showLoading(getString(R.string.txt_login_process));
        Map users = new HashMap();
        users.put("username", dataStoreApp.getUserName());
        users.put("password", dataStoreApp.getPassword() );
        users.put("new_password", newPass);
        Call<JsonObject> call = serverNetWorkAPI.changePass(users);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>(){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mpdl.hideLoading();
                try{
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if(!status){
                        toastUtil.showToast(getString(R.string.txt_change_pass_not_success));
                        return;
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
                toastUtil.showToast(getString(R.string.txt_change_pass_success));
                dataStoreApp.createPassword(newPass);
                dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mpdl.hideLoading();
                toastUtil.showToast(getString(R.string.txt_error_common));
                Util.LOGD(tag, " Throwable is " + t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(600, 900);
        window.setGravity(Gravity.CENTER);
    }

    public boolean validate(){
        if(!dataStoreApp.getPassword().equals(edtOldPass.getText().toString())){
            toastUtil.showToast(getString(R.string.txt_password_incorrect));
            return false;
        }
        newPass = edtNewPass.getText().toString();
        if(TextUtils.isEmpty(newPass)) {
            edtNewPass.setError(getString(R.string.hint_password));
            return false;
        }
        if(!newPass.equals(edtReNewPass.getText().toString())){
            toastUtil.showToast(getString(R.string.re_password_error));
            return false;
        }
        return true;
    }
}
