package com.zlz.app.tecentavdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterRoomActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_room_num;       //房间号
    private Button btn_enter_room;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);
        et_room_num = findViewById(R.id.et_room_num);
        btn_enter_room = findViewById(R.id.btn_enter_room);
        btn_enter_room.setOnClickListener(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_enter_room:       //进入房间
                Intent intent = new Intent(this, RoomActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("roomid", Integer.valueOf(et_room_num.getText().toString()));
                startActivity(intent);
                break;
        }

    }
}
