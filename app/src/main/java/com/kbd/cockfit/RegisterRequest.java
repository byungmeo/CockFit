package com.kbd.cockfit;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://cockfit.dothome.co.kr/cockfit_registe.php";
    private Map<String, String>map;
    //private Map<String, String>parameters;

    public RegisterRequest(String user_id, String user_password,  String user_nickname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("user_password", user_password);
        map.put("user_nickname", user_nickname);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

}
