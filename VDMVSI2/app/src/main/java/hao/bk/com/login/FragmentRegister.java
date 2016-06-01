package hao.bk.com.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/24/2016.
 */
public class FragmentRegister extends Fragment {
    public final String tag = "FragmentRegister";
    EditText edtUserName, edtPassword, edtEmail, edtRepassword;
    Button btnRegister;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    RegisterSuccessItf mCallback;
    Activity activity;
    private MyProgressDialog mPrgdl;
    LoginActivity loginAct;
    ImageButton btnShowPass;
    String username;
    String pasword;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        loginAct = (LoginActivity)context;
        mPrgdl = new MyProgressDialog(loginAct);
        mCallback = (RegisterSuccessItf) loginAct;
        toastUtil = new ToastUtil(context);
        dataStoreApp = new DataStoreApp(loginAct);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View view){
        edtUserName = (EditText)view.findViewById(R.id.edt_username);
        edtPassword = (EditText)view.findViewById(R.id.edt_password);
        edtEmail = (EditText)view.findViewById(R.id.edt_email);
        edtRepassword = (EditText)view.findViewById(R.id.edt_re_password);
        btnRegister = (Button)view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                if(validate())
                    runRegister();
            }
        });
    }
    public boolean validate(){
        username = edtUserName.getText().toString();
        String email = edtEmail.getText().toString();
        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(email) ){
            edtUserName.setError(getString(R.string.txt_email_recerive_pass_hint));
            return false;
        }
        if(TextUtils.isEmpty(username))
            username = email;
        pasword = edtPassword.getText().toString();
        if(TextUtils.isEmpty(pasword)) {
            edtPassword.setError(getString(R.string.hint_password));
            return false;
        }
        String repass = edtRepassword.getText().toString();
        if(!pasword.equals(repass)) {
            toastUtil.showToast(getString(R.string.re_password_error));
            return false;
        }
        return true;
    }
    public void runRegister(){

        if(!UtilNetwork.checkInternet(loginAct, getString(R.string.check_internet))){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        mPrgdl.showLoading(getString(R.string.txt_process));
        Map users = new HashMap();
        users.put("username", username);
        users.put("password", pasword );
        Call<JsonObject> call = serverNetWorkAPI.register(users);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>(){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mPrgdl.hideLoading();
                try{
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if(!status){
                        toastUtil.showToast(getString(R.string.txt_register_not_success));
                        return;
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
                saveDataUser();
                mCallback.onRegisterSuccessItf(username, pasword);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mPrgdl.hideLoading();
                Util.LOGD(tag, " Throwable is " + t);
            }
        });
    }
    public void saveDataUser(){
        dataStoreApp.createUserName(username);
        dataStoreApp.createPassword(pasword);
    }

    public interface RegisterSuccessItf {
        public void onRegisterSuccessItf(String username, String password);
    }
}
