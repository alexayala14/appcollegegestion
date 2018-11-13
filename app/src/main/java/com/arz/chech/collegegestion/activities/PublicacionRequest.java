package com.arz.chech.collegegestion.activities;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PublicacionRequest extends StringRequest {
    private static final String PUBLICACION_REQUEST_URL="http://miltonzambra.com/Publicaciones.php";
    private Map<String, String> params;
    public PublicacionRequest(String rut, String id_prioridad, String asunto, String descripcion, Response.Listener<String> listener){
        super(Method.POST, PUBLICACION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("rut", rut);
        params.put("id_prioridad", id_prioridad);
        params.put("asunto", asunto);
        params.put("descripcion", descripcion);
    }

    @Override
    public Map<String, String> getParams(){ return params; }
}
