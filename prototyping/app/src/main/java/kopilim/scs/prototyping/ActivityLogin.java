package kopilim.scs.prototyping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {
    public Intent intent;
    private Button login, register, forgotpass;
    private ImageView adminImage;
    private EditText email, password;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);

        login = (Button) findViewById(R.id.loginBtn);
        register = (Button) findViewById(R.id.registerBtn);
        forgotpass = (Button) findViewById(R.id.forgotpasswordBtn);
        email = (EditText) findViewById(R.id.emailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        adminImage = (ImageView) findViewById(R.id.adminImage);

        //Get Connect to Firebase
        fireAuth = FirebaseAuth.getInstance();

        //Hide keypad when first view the page
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final ProgressDialog dialog = new ProgressDialog(ActivityLogin.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Input Email");
                    return;
                }
                if (TextUtils.isEmpty(passwordText)) {
                    password.setError("Input Password");
                    return;
                }

                dialog.setMessage("Checking Email and Password...");
                dialog.show();

                fireAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if (!task.isSuccessful()) {
                                    AlertDialog.Builder d = new AlertDialog.Builder(ActivityLogin.this);
                                    d.setTitle("Login Fail");
                                    d.setIcon(R.mipmap.ic_launcher);
                                    d.setMessage("Login Failure. Please check your Email or Password");
                                    d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do ntg
                                        }
                                    });
                                    d.show();
                                }// end of Login failure
                                else {
                                    email.setText("");
                                    password.setText("");
                                    //Go to Main Page
                                    intent = new Intent(ActivityLogin.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                        });

            }
        }); // ---- End of Login Button

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ActivityLogin.this, ActivityForgotPass.class);
                startActivity(intent);
            }
        });

        adminImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ActivityLogin.this, ActivityAdminLogin.class);
                startActivity(intent);
            }
        });


    }//End of onCreate


}
