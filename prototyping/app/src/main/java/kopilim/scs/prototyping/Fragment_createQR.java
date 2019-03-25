package kopilim.scs.prototyping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;


public class Fragment_createQR extends Fragment {

    private Button generateQRBtn;
    private ImageView img;
    private EditText textInput;
    private String m_Text = "";

    private static ProgressDialog progressDialog;

    public Fragment_createQR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_qr, container, false);
        generateQRBtn = (Button) v.findViewById(R.id.generate_qr);
        img = (ImageView) v.findViewById(R.id.qrImgView);
        textInput = (EditText) v.findViewById(R.id.editText);

        setHasOptionsMenu(true);
        registerForContextMenu(img);

        generateQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String text = textInput.getText().toString();

                if(TextUtils.isEmpty(text)){
                    textInput.setError("Please Input Data to Generate QR Code");
                    return;
                }

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    //Text Content put in the QR COde and generated with X = 100 Y = 100
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,100,100);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix); //create QR Code
                    img.setImageBitmap(bitmap);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);

                    alertDialog(bitmap);

                    textInput.setText("");

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.optional_click_list, menu);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //getActivity().getMenuInflater().inflate(R.menu.optional_click_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scanQR:
                if(img.getDrawable() == null){
                    //do ntg
                }else{
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                        Bitmap b1 = drawable.getBitmap();

                        //Bitmap bMap = BitmapFactory.decodeStream(b1);
                        int[] intArray = new int[b1.getWidth() * b1.getHeight()];
                        // copy pixel data from the Bitmap into the 'intArray' array
                        b1.getPixels(intArray, 0, b1.getWidth(), 0, 0, b1.getWidth(),
                                b1.getHeight());

                        LuminanceSource source = new RGBLuminanceSource(b1.getWidth(),
                                b1.getHeight(), intArray);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Reader reader = new MultiFormatReader();// use this otherwise
                        // ChecksumException

                        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
                        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

                        Result result = reader.decode(bitmap, decodeHints);
                        if(result.getText() == null){
                            Toast.makeText(getContext(),"Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Data ---> "+result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                break;
        }
        return super.onContextItemSelected(item);
    }

    public void alertDialog(final Bitmap bitmap){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Save QR Code");
        builder.setMessage("Please enter the name for the QR Code.");
        builder.setCancelable(false);

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);
        builder.setPositiveButton("OK", null);
        final AlertDialog dialog = builder.show();
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(input.getText())){
                    input.setError("Please enter name of the QR Code");
                }else{
                    if(isExternalStorageReadable()) {
                        if(isExternalStorageWritable()){
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            dialog.dismiss();
                            //saveImageUtils saveImageUtils = new saveImageUtils();
                            saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, input.toString());
                            //SaveImage(bitmap);
                            saveImage(bitmap,input.toString());
                            displayProgressDialog();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), "QR Code Saved",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }, 4000);
                        }else{
                            Toast.makeText(getContext(),"Storage Permission not Grant",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"Storage Permission not Grant",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void saveImage(Bitmap bitmap, String image_name){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/Your_Directory_Name";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = image_name + ".png";
        File file = new File(myDir, fname);
        System.out.println(file.getAbsolutePath());
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(getContext(), new String[]{file.getPath()}, new String[]{"image/png"}, null);
    }

    /*
    //Save Image
    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/1Saved_QR_Code");
        myDir.mkdirs();
        String fname = m_Text +".png";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshGallery(file);
    }

    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        getActivity().sendBroadcast(intent);
    }
    */
    //Old method of storing image to gallery


    /****************************************************
     *
     *                 Progress Dialog
     *
     **************************************************/
    public void displayProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("... Processing ...");
        progressDialog.setTitle("Create new QR Code");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
