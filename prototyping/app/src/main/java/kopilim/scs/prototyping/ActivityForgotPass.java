package kopilim.scs.prototyping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPass extends AppCompatActivity {
    private EditText emailText;
    private Button forgotPassBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_forgotpassword);

        emailText = (EditText)findViewById(R.id.emailforgotpassword);
        forgotPassBtn = (Button)findViewById(R.id.forgotpasswordBTN);

        firebaseAuth = FirebaseAuth.getInstance();

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailReset = emailText.getText().toString().trim();

                if(TextUtils.isEmpty(emailReset)){
                    emailText.setError("Input Email");
                    return;
                }

                else {

                    progressDialog = new ProgressDialog(ActivityForgotPass.this);
                    progressDialog.setMessage("Sending link to your Email...");
                    progressDialog.show();

                    firebaseAuth.sendPasswordResetEmail(emailReset)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try {
                                        Thread.sleep(1500);
                                        progressDialog.dismiss();

                                        if (task.isSuccessful()) {

                                            emailText.setText("");

                                            //Thread.sleep(1500);

                                            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityForgotPass.this);

                                            dialog.setTitle("Reset Password");
                                            dialog.setMessage("We have sent you instructions to reset your password." + "\n"
                                                    + "Please Check your Email");
                                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Do ntg
                                                }
                                            });
                                            //show the dialog message
                                            dialog.show();

                                        } else {

                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityForgotPass.this);
                                            dialog.setTitle("Error of Reseting Password");
                                            dialog.setMessage("Error of sending reset password link. Please try again.");
                                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Do ntg
                                                }
                                            });

                                            dialog.show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }//end of Task
                            });
                }
            }
        });

    }
}
