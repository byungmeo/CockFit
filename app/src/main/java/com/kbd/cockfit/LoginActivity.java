package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    private EditText login_editText_id, login_editText_pw;
    private Button register_button,login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText login_editText_id = (EditText) findViewById(R.id.login_editText_id);
        EditText login_editText_pw = (EditText) findViewById(R.id.login_editText_pw);

        Button login_button=(Button)findViewById(R.id.login_button_login);
        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String userID = login_editText_id.getText().toString();
                String userPass = login_editText_pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText( getApplicationContext(), "테스트", Toast.LENGTH_SHORT ).show();
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {  //로그인 성공시

                                String user_id = jsonObject.getString( "user_id" );
                                String user_password = jsonObject.getString( "user_password" );
                                String user_nickname = jsonObject.getString( "user_nickname" );


                                Toast.makeText( getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, MainActivity.class );

                                intent.putExtra( "user_id", user_id );
                                intent.putExtra( "user_password", user_password );
                                intent.putExtra( "user_nickname", user_nickname );

                                startActivity( intent );

                            } else {  //로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest( userID, userPass, responseListener );
                RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                queue.add( loginRequest );

            }
        });

        Button register_button=(Button)findViewById(R.id.login_button_register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}