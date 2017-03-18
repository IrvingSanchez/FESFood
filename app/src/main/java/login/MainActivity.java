package login;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.irving.login.R;

import principal.Home;

public class MainActivity extends AppCompatActivity {
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv = (TextView) findViewById(R.id.textView);
        Typeface tp = Typeface.createFromAsset(getAssets(),"fonts/FopiRush.ttf");
        txv.setTypeface(tp);
        txv.setTextSize(50);
        Intent reg = new Intent(this, Home.class);
        //startActivity(reg);
        //this.finish();
    }

    public void onClickRegistrar(View v)
    {
        Intent reg = new Intent(this, RegistroActivity.class);
        startActivity(reg);
    }

    public void onClickLogin(View v)
    {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
}
