package kopilim.scs.prototyping;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class fragment_about_this_project extends Fragment {
    private TextView linkText;


    public fragment_about_this_project() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_this_project, container, false);
        linkText = (TextView)v.findViewById(R.id.linkTextview);
        linkText.setPaintFlags(linkText.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://repo.uum.edu.my/20153/1/KMICe2016%20480%20485.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        return v;
    }

}
