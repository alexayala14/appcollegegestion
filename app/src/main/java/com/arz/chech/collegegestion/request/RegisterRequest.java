package com.arz.chech.collegegestion.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://miltonzambra.com/Register.php";
    private Map<String, String> params;
    public RegisterRequest(String nombre, String apellido, String rut, String password, String token, String id_perfil, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("apellido", apellido);
        params.put("rut", rut);
        params.put("password", password);
        params.put("token", token);
        params.put("id_perfil", id_perfil);
    }

    @Override
    public Map<String, String> getParams(){ return params; }
}
