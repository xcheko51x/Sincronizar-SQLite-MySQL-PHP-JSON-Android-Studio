package com.xcheko51x.syncsqlitemysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnSync, btnAgregar;
    EditText etProducto, etFechaVenta, etPrecio;

    RecyclerView rvServidor, rvLocal;

    List<Venta> listaServidor = new ArrayList<>();
    List<Venta> listaLocal = new ArrayList<>();

    AdapterServidor adapterServidor;
    AdapterLocal adapterLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btnSync = findViewById(R.id.btnSync);
        btnAgregar = findViewById(R.id.btnAgregar);
        etProducto = findViewById(R.id.etProducto);
        etFechaVenta = findViewById(R.id.etFecha);
        etPrecio = findViewById(R.id.etPrecio);

        rvServidor = findViewById(R.id.rvServidor);
        rvServidor.setLayoutManager(new GridLayoutManager(this, 1));
        rvLocal = findViewById(R.id.rvLocal);
        rvLocal.setLayoutManager(new GridLayoutManager(this, 1));

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = formato.format(new Date());
        etFechaVenta.setText(fecha);

        obtenerServidor();
        obtenerLocal();

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sincronizar();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etProducto.getText().toString().equals("") || etPrecio.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "SE DEBEN LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                } else {
                    agregarLocal();
                }
            }
        });
    }

    public void sincronizar() {
        JSONArray jsonArrayProducto = new JSONArray();
        for(int i = 0 ; i < listaLocal.size() ; i++) {
            JSONObject jsonObjectProducto = new JSONObject();
            try {
                jsonObjectProducto.put("producto", listaLocal.get(i).getProducto());
                jsonObjectProducto.put("fechaVenta", listaLocal.get(i).getFechaVenta());
                jsonObjectProducto.put("precio", listaLocal.get(i).getPrecio());
                jsonObjectProducto.put("sincronizado", listaLocal.get(i).getSincronizado());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArrayProducto.put(jsonObjectProducto);
        }
        JSONObject json = new JSONObject();
        try {
            json.put("Productos", jsonArrayProducto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = json.toString();
        registrarServidor(jsonStr);
    }

    public void registrarServidor(final String json) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_REGISTRAR_SERVIDOR), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("OK")) {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
                    SQLiteDatabase db = admin.getWritableDatabase();
                    admin.borrarRegistros(db);

                    obtenerServidor();
                    obtenerLocal();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("json", json);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void agregarLocal() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("producto", etProducto.getText().toString());
        registro.put("fechaVenta", etFechaVenta.getText().toString());
        registro.put("precio", etPrecio.getText().toString());
        registro.put("sincronizado", "false");

        db.insert("ventas", null, registro);

        etProducto.setText("");
        etPrecio.setText("");

        db.close();

        obtenerLocal();

    }

    public void obtenerServidor() {
        listaServidor.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.URL_OBTENER_VENTAS),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Ventas");

                    for(int i = 0 ; i < jsonArray.length() ; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        listaServidor.add(
                                new Venta(
                                        jsonObject1.getString("idVenta"),
                                        jsonObject1.getString("producto"),
                                        jsonObject1.getString("fechaVenta"),
                                        jsonObject1.getString("precio"),
                                        jsonObject1.getString("sincronizado")
                                )
                        );
                    }

                    adapterServidor = new AdapterServidor(MainActivity.this, listaServidor);
                    rvServidor.setAdapter(adapterServidor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);
    }

    public void obtenerLocal() {
        listaLocal.clear();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("select * from ventas", null);
        if(fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                listaLocal.add(
                        new Venta(
                                fila.getString(0),
                                fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4)
                        )
                );
            } while (fila.moveToNext());

            adapterLocal = new AdapterLocal(MainActivity.this, listaLocal);
            rvLocal.setAdapter(adapterLocal);
        } else {
            adapterLocal = new AdapterLocal(MainActivity.this, listaLocal);
            rvLocal.setAdapter(adapterLocal);
            Toast.makeText(this, "No hay registros", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
