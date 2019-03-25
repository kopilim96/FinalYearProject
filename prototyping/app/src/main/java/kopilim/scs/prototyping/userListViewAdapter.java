package kopilim.scs.prototyping;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class userListViewAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;
    private TextView idTxt, emailTxt, nameTxt, icTxt;

    public userListViewAdapter(Activity context, List<User> userList) {
        super(context, R.layout.user_listview, userList); //User List View Layout
        this.context = context;
        this.userList = userList;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.user_listview, null, true);

        idTxt = (TextView)listView.findViewById(R.id.idListView);
        emailTxt = (TextView)listView.findViewById(R.id.emailListView);
        nameTxt = (TextView)listView.findViewById(R.id.nameListView);
        icTxt = (TextView)listView.findViewById(R.id.icListView);

        User user = userList.get(position);

        idTxt.setText(user.getId());
        emailTxt.setText(user.getEmail());
        nameTxt.setText(user.getName());
        icTxt.setText(user.getIc());

        return listView;
    }
}
