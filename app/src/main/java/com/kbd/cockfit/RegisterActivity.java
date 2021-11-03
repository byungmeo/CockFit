package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout con;
    private ImageView back;

    private EditText register_editText_id, register_editText_pw, register_editText_pw2, register_editText_nic;
    private Button register_button_checkId, register_button_regist;

    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        con=(ConstraintLayout)findViewById(R.id.register_layout_const);
        back = (ImageView)findViewById(R.id.register_button_backButton);

        final InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.hideSoftInputFromWindow(con.getWindowToken(),0);
            }
        });

        EditText register_editText_id = (EditText) findViewById(R.id.register_editText_id);
        EditText register_editText_pw = (EditText) findViewById(R.id.register_editText_pw);
        EditText register_editText_pw2 = (EditText) findViewById(R.id.register_editText_pw2);
        EditText register_editText_nic = (EditText) findViewById(R.id.register_editText_nic);

        register_button_checkId = (Button) findViewById(R.id.register_button_checkId);
        register_button_checkId.setOnClickListener(new View.OnClickListener() {  //ID 중복체크
            @Override
            public void onClick(View view) {

                String userID = register_editText_id.getText().toString();

                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder( RegisterActivity.this );
                                dialog = builder.setMessage("사용 가능한 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                register_editText_id.setEnabled(false);
                                validate = true;
                                register_button_checkId.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ValidateRequest validateRequest = new ValidateRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);

            }
        });


        register_button_regist = (Button) findViewById(R.id.register_button_regist);
        register_button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validate) {
                    Toast.makeText( getApplicationContext(), "아이디 중복 확인을 해야합니다.", Toast.LENGTH_SHORT ).show();
                    return;
                }

                String userID = register_editText_id.getText().toString();
                final String userPass = register_editText_pw.getText().toString();
                final String passCheck = register_editText_pw2.getText().toString();
                String userNic = register_editText_nic.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(userPass.length() == 0) {
                                Toast.makeText( getApplicationContext(), "비밀번호를 입력해야 합니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                            if(!userPass.equals(passCheck)) {

                                Toast.makeText( getApplicationContext(), "비밀번호를 확인해야 합니다.", Toast.LENGTH_SHORT ).show();
                                return;

                            }

                            if(userNic.length() == 0) {

                                Toast.makeText( getApplicationContext(), "닉네임을 입력해야 합니다.", Toast.LENGTH_SHORT ).show();
                                return;

                            }

                            if(success) {  //회원가입 성공시

                                Toast.makeText( getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( RegisterActivity.this, LoginActivity.class );

                                startActivity( intent );

                            } else {  //회원가입 실패시

                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                return;

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest( userID, userPass, userNic, responseListener);
                RequestQueue queue = Volley.newRequestQueue( RegisterActivity.this );
                queue.add( registerRequest );

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
