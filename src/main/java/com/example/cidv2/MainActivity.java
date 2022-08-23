package com.example.cidv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cidv2.Objetos.Trabajadores;
import com.example.cidv2.Objetos.Device;
import com.example.cidv2.Objetos.ProcesosPHP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGuardar;
    private Button btnListar;
    private Button btnLimpiar;
    private TextView txtNombre;
    private TextView txtProfesion;
    private TextView txtCelular;
    private CheckBox cbkEstado;
    private Trabajadores savedContacto;
    ProcesosPHP php;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setEvents();
    }
    public void initComponents() {
        this.php = new ProcesosPHP();
        php.setContext(this);
        this.txtNombre = findViewById(R.id.txtNombre);
        this.txtProfesion = findViewById(R.id.txtProfesion);
        this.txtCelular = findViewById(R.id.txtCel);
        this.cbkEstado = findViewById(R.id.cbxDisponibilidad);
        this.btnGuardar = findViewById(R.id.btnGuardar);
        this.btnListar = findViewById(R.id.btnListar);
        this.btnLimpiar = findViewById(R.id.btnLimpiar);
        savedContacto = null;
    }
    public void setEvents() {
        this.btnGuardar.setOnClickListener(this);
        this.btnListar.setOnClickListener(this);
        this.btnLimpiar.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(isNetworkAvailable()){
            switch (view.getId()) {
                case R.id.btnGuardar:
                    boolean completo = true;
                    if(txtNombre.getText().toString().equals("")){
                        txtNombre.setError("Introduce el Nombre");
                        completo=false;
                    }
                    if(txtProfesion.getText().toString().equals("")){
                        txtProfesion.setError("Introduce la Profesion");
                        completo=false;
                    }
                    if(txtCelular.getText().toString().equals("")){
                        txtCelular.setError("Introduce el Celular");
                        completo=false;
                    }
                    if (completo){
                        Trabajadores nContacto = new Trabajadores();
                        nContacto.setNombre(txtNombre.getText().toString());
                        nContacto.setArea_trabajo(txtProfesion.getText().toString());
                        nContacto.setCelular(txtCelular.getText().toString());
                        nContacto.setDisponibilidad(cbkEstado.isChecked() ? 1 : 0);
                        nContacto.setIdMovil(Device.getSecureId(this));
                        if(savedContacto == null){
                            php.insertarContactoWebService(nContacto);
                            Toast.makeText(getApplicationContext(), "Contacto guardado correctamente", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }else{
                            php.actualizarContactoWebService(nContacto,id);
                            Toast.makeText(getApplicationContext(),"Contacto actualizado correctamente", Toast.LENGTH_SHORT).show();
                            limpiar();
                        }
                    }
                    break;
                case R.id.btnLimpiar:
                    limpiar();
                    break;
                case R.id.btnListar:
                    Intent i= new Intent(MainActivity.this,ListaActivity.class);
                    limpiar();
                    startActivityForResult(i,0);
                    break;
            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesita tener conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
    public void limpiar(){
        savedContacto = null;
        txtNombre.setText("");
        txtProfesion.setText("");
        txtCelular.setText("");
        cbkEstado.setChecked(false);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(intent != null){
            Bundle oBundle = intent.getExtras();
            if(Activity.RESULT_OK == resultCode){
                Trabajadores contacto = (Trabajadores) oBundle.getSerializable("contacto");
                savedContacto = contacto;
                id = contacto.getId();
                txtNombre.setText(contacto.getNombre());
                txtProfesion.setText(contacto.getArea_trabajo());
                txtCelular.setText(contacto.getCelular());
                if(contacto.getDisponibilidad()>0){cbkEstado.setChecked(true);}
            }else{
                limpiar();
            }
        }
    }
}