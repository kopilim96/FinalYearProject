package kopilim.scs.prototyping;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.w3c.dom.Text;


public class fragment_result_colorQR extends Fragment {
    private ImageView imageView;
    private TextView textView, textResult;

    public fragment_result_colorQR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_result_color_qr, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageResult);
        textView = (TextView) v.findViewById(R.id.resultText);
        textResult = (TextView) v.findViewById(R.id.textResultColorQR);

        String udata = "Result:";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        textResult.setText(content);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });


        Bitmap bitmap = CachePot.getInstance().pop(Bitmap.class);
        if (bitmap == null) {
            Toast.makeText(getContext(), "Image is Empty", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Bitmap selectedImage1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            int width = selectedImage1.getWidth();
            int height = selectedImage1.getHeight();
            int pixel[] = new int[width * height];
            selectedImage1.getPixels(pixel, 0, width, 0, 0, width, height);
            RGBLuminanceSource source1 = new RGBLuminanceSource(width,
                    height, pixel);
            BinaryBitmap binaryBitmap1 = new BinaryBitmap(new HybridBinarizer(source1));
            Reader reader1 = new MultiFormatReader();
            Result result1 = null;
            try {
                result1 = reader1.decode(binaryBitmap1);
                if (result1.getText() != null) {
                    String text = result1.getText();
                    textView.setText(text);
                } else {
                    textView.setText("The colored QR Code might having problem to scan. " +
                            "\nCurrently, there are still having limited tool that would able to generate a colored QR Code." +
                            "\nHowever, current field will need more upgrade for improvement.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return v;
    }//End of oncreate
}
