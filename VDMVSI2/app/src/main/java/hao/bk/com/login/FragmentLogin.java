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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

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
 * Created by T430 on 3/18/2016.
 */
public class FragmentLogin extends Fragment {
    public final String tag = "FragmentLogin";
    AutoCompleteTextView edtUserName;
    EditText edtPassword;
    Button btnLogin;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    LoginSuccessItf mCallback;
    Activity activity;
    private MyProgressDialog mPrgdl;
    LoginActivity loginAct;
    ImageButton btnShowPass;
    String username;
    String pasword;
    private boolean isHidePassword;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        loginAct = (LoginActivity)context;
        mPrgdl = new MyProgressDialog(loginAct);
        mCallback = (LoginSuccessItf) loginAct;
        toastUtil = new ToastUtil(loginAct);
        dataStoreApp = new DataStoreApp(loginAct);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View view){
        isHidePassword = true;
        edtUserName = (AutoCompleteTextView)view.findViewById(R.id.edt_username);
        edtPassword = (EditText)view.findViewById(R.id.edt_password);
        edtPassword.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(getContext().INPUT_METHOD_SERVICE);
        username = dataStoreApp.getUserName();
        pasword = dataStoreApp.getPassword();
        if(!TextUtils.isEmpty(username)) {
            edtUserName.setText(username);
            edtPassword.requestFocus();
            edtPassword.setText(pasword);
        }
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pasword)){
            runLogin(true);
        }
        btnLogin = (Button)view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                runLogin(false);
            }
        });
        btnShowPass = (ImageButton)view.findViewById(R.id.btn_show_pass);
        btnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHidePassword) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isHidePassword = false;
                } else {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isHidePassword = true;
                }
            }
        });
    }

    public void runLogin(final boolean isAutoLogin){
        if(!isAutoLogin){
            username = edtUserName.getText().toString();
            pasword = edtPassword.getText().toString();
        }
        if(TextUtils.isEmpty(username)){
            edtUserName.setError(getString(R.string.hint_email_login));
            return;
        }
        if(TextUtils.isEmpty(pasword)){
            edtPassword.setError(getString(R.string.hint_password));
            return;
        }
        if(!UtilNetwork.checkInternet(loginAct, getString(R.string.check_internet))){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        mPrgdl.showLoading(getString(R.string.txt_login_process));
        Map users = new HashMap();
        users.put("username", username);
        users.put("password", pasword );
        Call<JsonObject> call = serverNetWorkAPI.login(users);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>(){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mPrgdl.hideLoading();
                Util.LOGD(tag, " response is " + response.body());
                try{
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if(!status){
                        toastUtil.showToast(getString(R.string.txt_login_not_success));
                        return;
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }

                if(!isAutoLogin){
                    saveDataUser();
                }
                mCallback.onLoginSuccess(username, pasword);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mPrgdl.hideLoading();
                Util.LOGD(tag, " Throwable is " + t);
                loginError();
            }
        });
    }
    public void saveDataUser(){
        dataStoreApp.createUserName(username);
        dataStoreApp.createPassword(pasword);
    }
    public void loginError(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(loginAct);
        builder.setMessage(getString(R.string.txt_error_common));
        builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                runLogin(false);
            }
        });
        builder.setNegativeButton(getString(R.string.btn_cancel_en), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.PrimaryDarkColor));
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.PrimaryDarkColor));
            }
        });
        alertDialog.show();
    }
    public interface LoginSuccessItf {
        public void onLoginSuccess(String username, String password);
    }

}
