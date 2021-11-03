package com.kbd.cockfit;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://cockfit.dothome.co.kr/cockfit_uservalidate.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public ValidateRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", user_id);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

}
