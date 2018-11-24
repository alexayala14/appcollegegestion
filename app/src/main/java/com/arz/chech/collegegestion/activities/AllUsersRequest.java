package com.arz.chech.collegegestion.activities;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AllUsersRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://miltonzambra.com/ConsultarListaUsuarios.php";
    private Map<String, String> params;
    public AllUsersRequest(String token, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("token", token);
    }

    @Override
    public Map<String, String> getParams(){ return params; }
}
