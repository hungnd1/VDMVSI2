package hao.bk.com.vdmvsi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.login.LoginActivity;
import hao.bk.com.models.UserObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentMoreTab extends Fragment {
    MainActivity main;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    TextView tvUserName, tvEmail;
    TextView btnChangePass, btnLogout;
    CircleImageView ivAvatar;
    ImageView ivBkgHeader;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
        toastUtil = new ToastUtil(context);
        dataStoreApp = new DataStoreApp(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more_tab, container, false);
        initViews(view);
        return view;
    }
    public void initViews(View v){
        if(dataStoreApp == null){
            dataStoreApp = new DataStoreApp(getContext());
        }
        tvUserName = (TextView)v.findViewById(R.id.tv_name_user);
        tvEmail = (TextView)v.findViewById(R.id.tv_email);
        ivAvatar = (CircleImageView)v.findViewById(R.id.iv_user_avatar);
        ivBkgHeader = (ImageView)v.findViewById(R.id.iv_user_bkg);
        btnChangePass = (TextView)v.findViewById(R.id.btn_change_pass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                FragmentChangePass fragmentChangePass =  FragmentChangePass.newInstance();
                fragmentChangePass.show(main.getFragmentManager(), "");
            }
        });
        btnLogout = (TextView)v.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                dataStoreApp.createPassword("");
                Intent intent = new Intent(main, LoginActivity.class);
                startActivity(intent);
                main.finish();
            }
        });
        getUserInfo();
    }

    public void getUserInfo(){
        if(!UtilNetwork.checkInternet(main)){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getUser);
        hashMap.put("username", dataStoreApp.getUserName());
        Call<JsonObject> call = stackOverflowAPI.getUserInfo(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Util.LOGD("24_4", response.body().toString());
                UserObj userObj = JsonCommon.getUserInfo(response.body().getAsJsonArray("data"));
                tvUserName.setText(userObj.getUserName());
                tvEmail.setText(userObj.getEmail());
               try {
                   Picasso.with(main)
                           .load(userObj.getUrlAvatar())
                           .into(new Target() {
                               @Override
                               public void onPrepareLoad(Drawable placeHolderDrawable) {

                               }

                               @Override
                               public void onBitmapFailed(Drawable errorDrawable) {

                               }

                               @Override
                               public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                                   //Set it in the ImageView
                                   ivAvatar.setImageBitmap(bitmap);
                                   ivBkgHeader.setImageBitmap(bitmap);
                               }
                           });
               }catch (Exception e){

               }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " +t);
            }
        });
    }
}
