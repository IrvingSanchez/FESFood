package login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irving.login.R;

import principal.Home;

public class ComprobarRegistro extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String phone;
    Integer iCodigo;

    private EditText celular,eCodigo;
    private Button boton;
    private boolean fueEnviado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobar_registro);
        phone = getIntent().getExtras().getSerializable("Telefono").toString();
        iCodigo = (int)(Math.random()*9998 + 1);

        celular = (EditText) findViewById(R.id.celular);
        eCodigo = (EditText) findViewById(R.id.codigo);
        boton = (Button) findViewById(R.id.button);

        celular.setText(phone);

    }

    public void onClickButton(View v)
    {
        if(fueEnviado) {
            if (eCodigo.getText().length() != 0)
            {
                //  Compara
                if (iCodigo == Integer.parseInt(eCodigo.getText().toString())) {
                    Toast.makeText(this, "Comprobación exitosa", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, Home.class);
                    startActivity(i);
                    this.finish();
                }

            }
            else
            {
                eCodigo.requestFocus();
                eCodigo.setError("Campo requerido");
            }


        }
        else {
            enviarCodigo();
            boton.setText("Comprobar");
        }
    }

    public void enviarCodigo()
    {
        String mensaje = "FES Food \n" +
                "Tu código de verificación es: " + iCodigo.toString();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone,null,mensaje,null,null);
        Toast.makeText(this,"Codigo enviado",Toast.LENGTH_LONG).show();
        fueEnviado = true;
    }


}
