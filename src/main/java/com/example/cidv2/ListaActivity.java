package com.example.cidv2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cidv2.Objetos.Trabajadores;
import com.example.cidv2.Objetos.Device;
import com.example.cidv2.Objetos.ProcesosPHP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListaActivity extends ListActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private AppCompatButton btnNuevo;
    private final Context context = this;
    private ProcesosPHP php = new ProcesosPHP();;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Trabajadores> listaContactos;
    private String serverip = "https://motslatam.com/CID/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        listaContactos = new ArrayList<Trabajadores>();
        request = Volley.newRequestQueue(context);
        consultarTodosWebService();
        btnNuevo = findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    public void consultarTodosWebService(){
        String url = serverip + "wsConsultarTodos.php?idMovil="+ Device.getSecureId(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
    }
    @Override
    public void onResponse(JSONObject response) {
        Trabajadores contacto = null;
        JSONArray json = response.optJSONArray("contactos");
        try {
            for(int i=0;i<json.length();i++){
                contacto = new Trabajadores();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                contacto.setId(jsonObject.optInt("id"));
                contacto.setNombre(jsonObject.optString("nombre"));
                contacto.setArea_trabajo(jsonObject.optString("profesion"));
                contacto.setCelular(jsonObject.optString("celular"));
                contacto.setDisponibilidad(jsonObject.optInt("estado"));
                contacto.setIdMovil(jsonObject.optString("idMovil"));
                listaContactos.add(contacto);
            }
            MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_trabajador,listaContactos);
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    class MyArrayAdapter extends ArrayAdapter<Trabajadores> {
        Context context;
        int textViewRecursoId;
        ArrayList<Trabajadores> objects;
        public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<Trabajadores> objects){
            super(context, textViewResourceId, objects);
            this.context = context;
            this.textViewRecursoId = textViewResourceId;
            this.objects = objects;
        }
        public View getView(final int position, View convertView, ViewGroup
                viewGroup){
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textViewRecursoId, null);
            TextView lblNombre = (TextView) view.findViewById(R.id.lblNombreCompleto);
            TextView lblProfesion = (TextView) view.findViewById(R.id.lblAreaTrabajo);
            TextView lblCelular = (TextView) view.findViewById(R.id.lblCelular);
            AppCompatButton btnModificar = (AppCompatButton) view.findViewById(R.id.btnModificar);
            AppCompatButton btnBorrar = (AppCompatButton) view.findViewById(R.id.btnBorrar);
            if(objects.get(position).getDisponibilidad()>0){
                lblNombre.setTextColor(Color.YELLOW);
                lblProfesion.setTextColor(Color.YELLOW);
                lblCelular.setTextColor(Color.YELLOW);
            }else{
                lblNombre.setTextColor(Color.WHITE);
                lblProfesion.setTextColor(Color.WHITE);
                lblCelular.setTextColor(Color.WHITE);
            }
            lblNombre.setText(objects.get(position).getNombre());
            lblProfesion.setText(objects.get(position).getArea_trabajo());
            lblCelular.setText(objects.get(position).getCelular());
            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    php.setContext(context);
                    Log.i("id", String.valueOf(objects.get(position).getId()));
                    php.borrarContactoWebService(objects.get(position).getId());
                    objects.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Contacto eliminado con exito", Toast.LENGTH_SHORT).show();
                }
            });
            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle oBundle = new Bundle();
                    oBundle.putSerializable("contacto", objects.get(position));
                    Intent i = new Intent(ListaActivity.this,MainActivity.class);
                    i.putExtras(oBundle);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            });
            return view;
        }
    }
}