package kopilim.scs.prototyping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public boolean doubleBackLable = false;
    public Fragment fragment = null;
    public Class fragmentClass = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuth;
    private TextView emailHeader;

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<information> informationList;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Main Layout for Activity Main
        setContentView(R.layout.nav_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        informationList = new ArrayList<>();
        adapter = new CardAdapter(this, informationList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                information information = informationList.get(position);
                switch (position) {
                    case 0:
                        //What is QR Code
                        fragmentClass = fragment_info_qr.class;
                        break;
                    case 1:
                        //What is Color QR Code
                        fragmentClass = fragment_info_color_qr.class;
                        break;
                    case 2:
                        //About this project
                        fragmentClass = fragment_about_this_project.class;
                        break;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.viewPage, fragment).addToBackStack(null).commit();

            }

        }));

        prepareAlbums();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        setupDrawerContent(navigationView);

        //Cover Pic
        try {
            Glide.with(this).load(R.drawable.backgroundimg).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    emailHeader = (TextView) findViewById(R.id.emailHeader);
                    emailHeader.setText(email);
                }
            }
        };
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("QR Code Scanner");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.information_qr_code,
                R.drawable.information_color_qr_code,
                R.drawable.information_project,
        };

        information a = new information("What is QR Code", covers[0]);
        informationList.add(a);

        a = new information("What is Color QR Code", covers[1]);
        informationList.add(a);

        a = new information("About this Project", covers[2]);
        informationList.add(a);


        adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.addAuthStateListener(mAuth);
    }


    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackLable) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Exit Method
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask(); //---> Activity End
            } else {
                finish();
            }
            System.exit(0);
            return;
        } else {
            if (fragment != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
                fragment = null;

            } else {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.reportBug) {
            reportBug();
            return true;

        } else if (id == R.id.logout) {
            progressDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    firebaseAuth.signOut();
                }
            }, 2000);

            finish(); //End Activity
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item click
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectedItemDrawer(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.scanQR:
                fragmentClass = Fragment_scanQR.class;
                break;

            case R.id.importFromGallery:
                fragmentClass = Fragment_importGallery.class;
                break;

            case R.id.encodeQR:
                fragmentClass = Fragment_createQR.class;
                break;

            case R.id.encodeColorQR:
                fragmentClass = Fragment_createColorQR.class;
                break;

            case R.id.decodeColor:
                fragmentClass = fragment_decode.class;
                break;

            case R.id.share:
                fragmentClass = Fragment_share.class;
                shareFunction();
                break;

            case R.id.send:
                fragmentClass = Fragment_send.class;
                sendFunction();
                break;

            case R.id.logout:
                Toast.makeText(this, "Login Out Successful",
                        Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                finish();
                System.exit(0);
                break;

            default:
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.viewPage, fragment).addToBackStack(null).commit();


        item.setChecked(false);
        drawer.closeDrawers();
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedItemDrawer(item);
                navigationView.setItemIconTintList(null);
                return true;
            }
        });
    }

    //-----------------------------------------
    //             Share to Public
    //-----------------------------------------
    protected void shareFunction() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, "Share"));
    }


    //------------------------------------------
    //          Send to Particular User
    //------------------------------------------
    protected void sendFunction() {
        Log.i("Send to Particular User", "");

        //to whom
        String[] TO = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); // Send to Who
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Sending Email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "Error of sending Email. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    //--------------------------------
    //      Send Email Report Bug
    //---------------------------------
    protected void reportBug() {
        Log.i("Send email", "");

        //to whom
        String[] TO = {"qrscanneradmin55@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); // Send to Who
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Bug");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please Describe Error or Bug here.");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Sending Email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "Error of sending Email. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    //---------------------------------
    //      ProgressDialog
    //--------------------------------
    public void progressDialog() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Processing...");
        dialog.show();
    }

    public void dismissDialog() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.dismiss();
    }
}
