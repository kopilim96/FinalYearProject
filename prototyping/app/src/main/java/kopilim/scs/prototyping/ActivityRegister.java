package kopilim.scs.prototyping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegister extends AppCompatActivity {
    User user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText email, pass1, pass2, name, icnum;
    private Button registerBtn, backtoLoginBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_register);

        email = (EditText) findViewById(R.id.emailRegister);
        pass1 = (EditText) findViewById(R.id.passwordRegister);
        pass2 = (EditText) findViewById(R.id.secondpasswordRegister);
        name = (EditText) findViewById(R.id.nameRegister);
        icnum = (EditText) findViewById(R.id.icRegister);

        registerBtn = (Button) findViewById(R.id.registerBTN);
        backtoLoginBtn = (Button) findViewById(R.id.backToLoginBTN);

        firebaseAuth = firebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        //DatabaseReference ref = firebaseDatabase.getReference("User");


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailText = email.getText().toString().trim();
                String pass1Text = pass1.getText().toString().trim();
                String pass2Text = pass2.getText().toString().trim();
                final String nameText = name.getText().toString().trim();
                final String icNumText = icnum.getText().toString().trim();


                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Input Email");
                    return;
                }
                if (TextUtils.isEmpty(pass1Text)) {
                    pass1.setError("Input Password");
                    return;
                }
                if (pass1Text.length() < 6) {
                    Toast.makeText(getApplicationContext(),
                            "Password too short, enter minimum 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass2Text)) {
                    pass2.setError("Input Password");
                    return;
                }
                if (!pass1Text.equalsIgnoreCase(pass2Text)) {
                    pass1.setError("Password not Matching");
                    return;
                }
                if (TextUtils.isEmpty(nameText)) {
                    name.setError("Input Name");
                    return;
                }
                if (TextUtils.isEmpty(icNumText)) {
                    icnum.setError("Input Ic Number");
                    return;
                }

                progressDialog = new ProgressDialog(ActivityRegister.this);
                progressDialog.setMessage("Progressing...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(emailText, pass1Text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            String id = databaseReference.push().getKey();
                            user = new User(id, emailText, nameText, icNumText);
                            databaseReference.child(id).setValue(user);

                            Toast.makeText(getApplicationContext(), "Create Account Successful",
                                    Toast.LENGTH_LONG).show();
                            clearTextField();
                            try {
                                Thread.sleep(1500);
                                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityRegister.this);
                            alertDialog.setTitle("Try Again");
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            alertDialog.setMessage("Please check and make sure the email is an un-register email to the system.");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do ntg
                                }
                            });
                            alertDialog.show();
                        }
                    }
                });
            }
        });     //End of Register Button

        backtoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });//Back to Login Page


    }// End of onCreate Method

    public void clearTextField() {
        email.setText("");
        pass1.setText("");
        pass2.setText("");
        icnum.setText("");
        name.setText("");
        //finish();
    }
}
