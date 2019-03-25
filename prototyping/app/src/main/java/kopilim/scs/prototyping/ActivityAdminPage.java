package kopilim.scs.prototyping;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdminPage extends AppCompatActivity {
    public boolean doubleBackLable = false;

    private ImageView logoutImage;
    private DatabaseReference databaseReference;
    private ListView listView;
    private User user;
    private List<User> userList;
    private String url = "https://console.firebase.google.com/project/prototyping-14ba8/authentication/users";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin_page);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        progressDialog = new ProgressDialog(ActivityAdminPage.this);
        progressDialog.setTitle("Loging Out");
        progressDialog.setMessage("...Loading...");
        progressDialog.setIcon(R.mipmap.ic_launcher);

        logoutImage = (ImageView) findViewById(R.id.adminLogout);
        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressDialog.create();
                    progressDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(!progressDialog.isShowing()) {
                                adminLogout();
                            }
                        }
                    }, 2000);
                }else {
                    adminLogout();
                }
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        userList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityAdminPage.this);
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setTitle("Are You Sure?");
                alertDialog.setMessage("Delete the User and the User will not longer able to Login to the " +
                        "System anymore.");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = userList.get(position);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("User").child(user.getId());
                        databaseReference.removeValue();

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);

                    }//end of YES Button Click
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                });

                alertDialog.create().show();
                return true;
            }//End of Long Click
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userListViewAdapter adapter = new userListViewAdapter(ActivityAdminPage.this, userList);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        if(doubleBackLable){
            Intent intent1 = new Intent(ActivityAdminPage.this, ActivityLogin.class);
            startActivity(intent1);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            //intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //Exit Method
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask(); //---> Activity End
            }else{
                finish();
            }
            System.exit(0);
            return;
        }else {
            this.doubleBackLable = true;
            Toast.makeText(getApplicationContext(), "Click BACK again to Exit"
                        , Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackLable = false;
                }
                }, 2000);
        }
    }


    public void adminLogout(){
        Intent intent = new Intent(ActivityAdminPage.this, ActivityLogin.class);
        startActivity(intent);
    }

}
