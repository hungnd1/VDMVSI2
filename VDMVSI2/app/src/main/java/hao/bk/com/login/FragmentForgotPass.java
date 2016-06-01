package hao.bk.com.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/24/2016.
 */
public class FragmentForgotPass extends Fragment {
    public final String tag = "FragmentForgotPass";
    EditText edtPassword;
    Button btnFogotPass;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    ForgotPassItf mCallback;
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
        //mCallback = (ForgotPassItf) loginAct;
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
        View content = inflater.inflate(R.layout.fragment_forgot_pass, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View view){
        btnFogotPass = (Button)view.findViewById(R.id.btn_forgot_pass);
        btnFogotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.showToast("Chưa có Api");
            }
        });
    }

    public void runRegister(){

    }
    public void saveDataUser(){
        dataStoreApp.createUserName(username);
        dataStoreApp.createPassword(pasword);
    }

    public interface ForgotPassItf {
        public void onReceivePassSuccess(String username, String password);
    }
}
