package com.zlz.app.tecentavdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMManager;
import com.tencent.av.sdk.AVContext;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {

    private TextView tv_info;
    private EditText et_num;        //登录账户
    private Button btn_enter;
    private LoginHelper loginHelper;
    private String userSig;
    private String userSig1 = "eJxlj0tPg0AUhff8CsJWI-PoSNsdUoKNlqSlXdjNhA4DXkUew0Cljf9dxSaS9G6-7*ScezZM07S2z9FdLETZFprrvpKWOTctZN3*w6qChMeaU5VcQflZgZI8TrVUA8SMMYLQ2IFEFhpSuBhtIxUe4SZ550PHX36CEJ44BNGxAtkAV-6Lt1wvPDvfO0w47go9HPPsEOIZKnPSBNNot6*Btgs4iYP7FKbZ8tUNA3xj134WSLaJHPvob-qTIPDYv029dV1se0Z2ZSfUfSdHlRo*5OUhOvsZRagzop1UDZTFIBCEGSYU-Z5lfBnfoj5cig__";
    private String userSig2 = "eJxlj11PwjAUhu-3K5reavS0W4F6twAKfs1uJAZulrKWUcc*6ArBGP*7Oklc4rl9njfvez48hBBePCZXMsvqQ*VS995ojG4QBnz5B5vGqFS61LfqH9Snxlidyo3TtoOEMUYB*o5RunJmY87GodWW9nCrirTr*M0HACQYUvD7isk7*DRdjudiPB3W22O4qJ753kYndXtvVtHgoeTJ3ZrxVxK3o*JaJk7tJvl8G0Zq9HJhxToWGdvXufTjYpXboIRZC6UYFGyyFLM4edPhrlfpTKnPD-n8exTl-c1HbVtTV51AgTBCffg57H16X-bDXN8_";
    private String userSig3 = "eJxlj0tPg0AUhff8ignbGjuPkopJF1hJrVJbbBMfG0JnBrlAAYepIzH*91ZsIsa7-b6Tc*6nhRCyN8H6POa82pc60m0tbXSJbGyf-cK6BhHFOmJK-IPyowYlozjRUnWQOI5DMe47IGSpIYGTsW*kYj3ciDzqOn7yI4zJaEzxHwVeO7jww*n86jHLwfA3lfmmmu6KlUlSPExd74HMny8yFjyF6trV*nZVe*CFAW*bYD3ONwOzbCHmwbJo7u*K2Xa49QemvFmkL8LlNJ6ZyaRXqWEnTw8x9ziKEadH36VqoCo7gWLiEMrw99nWl3UAhQxd3g__";
    private static final String TAG = "logininfo";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginHelper = new LoginHelper(this);
        

        tv_info = findViewById(R.id.tv_info);
        et_num = findViewById(R.id.et_num);
        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(this);
        tv_info.setText("iLiveSDK版本号信息："+"\n"+" iLiveSDK: "+ILiveSDK.getInstance().getVersion()+"\n IMSDK:"+
                TIMManager.getInstance().getVersion()+"\n AVSDK:"+
                AVContext.sdkVersion);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_enter:            //登录
                if(et_num.getText().toString().equals("user1")){
                    userSig=userSig1;
                    loginHelper.loginSDK(et_num.getText().toString(), userSig);
                }else if(et_num.getText().toString().equals("user2")){
                    userSig = userSig2;
                    loginHelper.loginSDK(et_num.getText().toString(), userSig);
                }else if(et_num.getText().toString().equals("user3")){
                    userSig = userSig3;
                    loginHelper.loginSDK(et_num.getText().toString(), userSig);
                }else {
                    Toast.makeText(getApplicationContext(),"该用户名不存在",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,EnterRoomActivity.class);
        intent.putExtra("username", et_num.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onLoginFailed(String module, int errCode, String errMsg) {
        Toast.makeText(getApplicationContext(),"登录失败"+"errMsg:"+errMsg,Toast.LENGTH_SHORT).show();
        Log.i(TAG, "错误号:"+errCode+"错误信息:"+errMsg);



    }
}
