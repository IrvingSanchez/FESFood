package login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import conexion.Constantes;
import com.example.irving.login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import conexion.VolleySingleton;

public class RegistroActivity extends AppCompatActivity {

    //  Referencia a los controles de la interfaz
    private EditText nombre, apellido, email, telefono, noCuenta;
    private Spinner s;
    private TextView estado;
    private ProgressBar pb;
    private LinearLayout verticalLayout;

    //  Variables
    private TareaRegistro registrar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //  Conectar variables
        nombre = (EditText) findViewById(R.id.nombre);
        apellido = (EditText) findViewById(R.id.apellido);
        email = (EditText) findViewById(R.id.email);
        telefono = (EditText) findViewById(R.id.celular);
        noCuenta = (EditText) findViewById(R.id.numCuenta);
        estado = (TextView) findViewById(R.id.textView);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        verticalLayout = (LinearLayout) findViewById(R.id.verticalLay);

        //  Llenar spinner

        s = (Spinner) findViewById(R.id.carrera);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.carreras_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapter);

        s.setSelection(5);
    }

    //  Metodo que se ejecuta al dar click al boton registrar

    public void registrarClick(View v) {
        if (!hayCamposVacios() && sonCamposValidos()) {
            //  Obtener nombre del control
            String sNombre = nombre.getText().toString();
            String sApellido = apellido.getText().toString();
            String sEmail = email.getText().toString();
            String stelefono = telefono.getText().toString();
            String numCuenta = noCuenta.getText().toString();
            String sCarrera = s.getSelectedItem().toString();

            //  Crear JSON con los datos obtenidos
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("nombre", sNombre);
                jObject.put("apellido", sApellido);
                jObject.put("email", sEmail);
                jObject.put("telefono", stelefono);
                jObject.put("numCuenta", numCuenta);
                jObject.put("carrera", sCarrera);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            registrar = new TareaRegistro(jObject, Constantes.INSERT,this);
            registrar.execute((Void) null);


        }
    }

    //  Metodo para verificar si hay campos sin llenar
    private boolean hayCamposVacios() {
        boolean r = true;

        if (nombre.getText().length() == 0) {
            nombre.requestFocus();
            nombre.setError("Este campo es requerido");
        } else if (apellido.getText().length() == 0) {
            apellido.requestFocus();
            apellido.setError("Este campo es requerido");
        } else if (email.getText().length() == 0) {
            email.requestFocus();
            email.setError("Este campo es requerido");
        } else if (telefono.getText().length() == 0) {
            telefono.requestFocus();
            telefono.setError("Este campo es requerido");
        } else if (noCuenta.getText().length() == 0) {
            noCuenta.requestFocus();
            noCuenta.setError("Este campo es requerido");
        } else if (s.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una carrera", Toast.LENGTH_SHORT).show();
        } else
            r = false;
        return r;
    }

    //  Metodo que verifica si los campos son validos
    private boolean sonCamposValidos() {
        Boolean r = false;

        if (!email.getText().toString().contains("@")) {
            email.requestFocus();
            email.setError("Campo invalido");
        } else if (noCuenta.length() < 9) {
            noCuenta.requestFocus();
            noCuenta.setError("Campo invalido");
        } else
            r = true;
        return r;
    }


    public void mostrarProgreso(Boolean mostrar)
    {
        if(mostrar)
        {
            verticalLayout.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        }
        else
        {
            verticalLayout.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }


    public class TareaRegistro extends AsyncTask<Void, Void, Boolean> {

        private final JSONObject jsonObj;
        private final String urlWebService;
        private final Context context;

        TareaRegistro(JSONObject json, String urlWebService, Context context) {
            this.jsonObj = json;
            this.context = context;
            this.urlWebService = urlWebService;
        }

        @Override
        protected void onPreExecute() {
            mostrarProgreso(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            VolleySingleton.getInstance(context).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.POST,
                            urlWebService,
                            jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Procesar la respuesta del servidor
                                    procesarRespuesta(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("error", "Error Volley: " + error.getMessage());
                                    mostrarProgreso(false);
                                    Toast.makeText(context.getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            }

                    ) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8" + getParamsEncoding();
                        }
                    }
            );
            return true;
        }

        //  Metodo que procesa la respuesta recibida por el servidor
        private void procesarRespuesta(JSONObject response) {


            try {
                // Obtener estado
                String estado = response.getString("estado");

                // Obtener mensaje
                String mensaje = response.getString("mensaje");

                mostrarProgreso(false);

                switch (estado) {
                    case "1":
                        // Mostrar mensaje
                        Toast.makeText(
                                context,
                                mensaje,
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context.getApplicationContext(),ComprobarRegistro.class);
                        String numero = telefono.getText().toString();
                        i.putExtra("Telefono",numero);
                        context.startActivity(i);
                        finish();

                        break;

                    case "2":
                        // Mostrar mensaje
                        Toast.makeText(
                                context,
                                mensaje,
                                Toast.LENGTH_SHORT).show();
                        Intent i2 = new Intent(context.getApplicationContext(),RegistroActivity.class);
                        context.startActivity(i2);
                        finish();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
