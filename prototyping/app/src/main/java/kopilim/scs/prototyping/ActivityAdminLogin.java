package kopilim.scs.prototyping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAdminLogin extends AppCompatActivity {
    private EditText adminUsername, adminPassword;
    private Button adminLoginBtn;

    public Class fragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin_login_page);

        adminLoginBtn = (Button)findViewById(R.id.adminLoginBtn);
        adminUsername = (EditText)findViewById(R.id.adminUsernameLogin);
        adminPassword = (EditText)findViewById(R.id.adminPasswordLogin);

        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminUsernameText = adminUsername.getText().toString().trim();
                String adminPasswordText = adminPassword.getText().toString().trim();

                if(TextUtils.isEmpty(adminUsernameText)){
                    adminUsername.setError("Input Admin Username");
                    return;
                }

                if(TextUtils.isEmpty(adminPasswordText)){
                    adminPassword.setError("Input Admin Password");
                    return;
                }

                if((adminUsernameText.equalsIgnoreCase("admin"))&&
                        (adminPasswordText.equalsIgnoreCase("root"))){
                    adminUsername.setText("");
                    adminPassword.setText("");
                    Toast.makeText(ActivityAdminLogin.this, "Admin Login Successful.. Have a Good Day",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivityAdminLogin.this, ActivityAdminPage.class);
                    startActivity(intent);
                }
                if(!adminPasswordText.equalsIgnoreCase("root")){
                    adminUsername.setText("");
                    adminPassword.setText("");
                    Toast.makeText(ActivityAdminLogin.this,
                            "Admin Login Failure... Please check Again..", Toast.LENGTH_LONG).show();
                    //Intent to admin Page.
                }
            }
        });

    }//end of onCreate

}
