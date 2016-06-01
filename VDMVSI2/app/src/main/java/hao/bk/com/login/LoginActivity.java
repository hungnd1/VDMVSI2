package hao.bk.com.login;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;
/**
 * Created by T430 on 4/20/2016.
 */
public class LoginActivity extends AppCompatActivity implements FragmentLogin.LoginSuccessItf, FragmentRegister.RegisterSuccessItf {
    FragmentLogin frmLogin = null;
    FragmentRegister frmRegister = null;
    FragmentForgotPass frmForgotPass = null;
    ToastUtil toastUtil;
    Button btnRegister, btnForgotPass;
    DataStoreApp dataStoreApp;
    LinearLayout lnlBtnBottom;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        frmLogin = new FragmentLogin();
        loadFragment(frmLogin);
    }
    public void initViews(){
        dataStoreApp = new DataStoreApp(this);
        toastUtil = new ToastUtil(this);
        lnlBtnBottom = (LinearLayout)findViewById(R.id.lnl_bottom);
        btnForgotPass = (Button)findViewById(R.id.btn_fogot_pass);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                if(frmForgotPass == null)
                    frmForgotPass = new FragmentForgotPass();
                loadFragment(frmForgotPass);
                showLnlBtnBottom(View.INVISIBLE);
            }
        });
        btnRegister = (Button)findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                if(frmRegister == null)
                    frmRegister = new FragmentRegister();
                loadFragment(frmRegister);
                showLnlBtnBottom(View.INVISIBLE);
            }
        });
    }
    public void showLnlBtnBottom(int v){
        lnlBtnBottom.setVisibility(v);
        findViewById(R.id.divider).setVisibility(v);
    }
    // load fragment
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frm_container, fragment)
                .commit();
    }

    @Override
    public void onLoginSuccess(String username, String password) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRegisterSuccessItf(String username, String password) {
        toastUtil.showToast(getString(R.string.txt_success_register));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        if(!lnlBtnBottom.isShown()){
            loadFragment(frmLogin);
            showLnlBtnBottom(View.VISIBLE);
        } else {
            finish();
        }
    }
}

