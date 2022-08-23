package com.example.cidv2.Objetos;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;

public class ProcesosPHP implements Response.Listener<JSONObject>,Response.ErrorListener {
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Trabajadores> empleados = new ArrayList<Trabajadores>();
    private String serverip = "https://motslatam.com/CID/";
    public void setContext(Context context){
        request = Volley.newRequestQueue(context);
    }
    public void insertarContactoWebService(Trabajadores c){
        String url = serverip + "wsRegistro.php?nombre="+c.getNombre()+"&profesion="+c.getArea_trabajo()+"&celular="+c.getCelular()+"&estado="+c.getDisponibilidad()+"&idMovil="+c.getIdMovil();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void actualizarContactoWebService(Trabajadores c,int id){
        String url = serverip + "wsActualizar.php?id="+id+"&nombre="+c.getNombre()+"&profesion="+c.getArea_trabajo()+"&celular="+c.getCelular()+"&estado="+c.getDisponibilidad();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void borrarContactoWebService(int id){
        String url = serverip + "wsEliminar.php?id="+id;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("ERROR",error.toString());
    }
    @Override
    public void onResponse(JSONObject response) {
    }
}
