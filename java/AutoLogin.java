import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    
    EditText username, password;
    Button loginButton;
    String user, pass;
    SharedPreferences sp;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed(){
        System.exit(0);
            }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);
        sp=getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("logged",false)){
            startActivity(new Intent(Login.this, Users.class));

        }
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                //code for login button
                    }
                //saving in the shared preferences
                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putString("username",user).apply();
            }
        });
    }
}
