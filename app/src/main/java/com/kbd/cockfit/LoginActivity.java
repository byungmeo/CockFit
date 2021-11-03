package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_id; //id 입력창
    private EditText editText_password; //pw 입력창
    private Button button_login; //로그인 버튼
    private Button button_register; //회원가입 버튼
    private Button button_develop; //개발자용 임시 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CockFit); //Splash 화면 출력 후 본래 테마로 전환
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_id = findViewById(R.id.login_editText_id);
        editText_password = findViewById(R.id.login_editText_pw);
        button_login = findViewById(R.id.login_button_login);
        button_register =findViewById(R.id.login_button_register);
        button_develop = findViewById(R.id.login_button_developButton);

        //로그인 버튼에 리스너를 부착합니다.
        button_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String userID = editText_id.getText().toString();
                String userPass = editText_password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSuccess = jsonObject.getBoolean("success");

                            if(isSuccess) {  //로그인 성공시
                                String user_id = jsonObject.getString("user_id");
                                String user_password = jsonObject.getString("user_password");
                                String user_nickname = jsonObject.getString("user_nickname");


                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                intent.putExtra("user_id", user_id);
                                intent.putExtra("user_password", user_password);
                                intent.putExtra("user_nickname", user_nickname);

                                startActivity( intent );
                            } else {  //로그인 실패시
                                Log.d("test", "로그인 실패");
                                Toast.makeText( getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

        //회원가입 버튼에 리스너를 부착합니다.
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity( intent );
            }
        });

        //개발자용 임시 버튼에 리스너를 부착합니다.
        //로그인 로직을 건너뜁니다.
        button_develop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity( intent );
            }
        });
    }
}