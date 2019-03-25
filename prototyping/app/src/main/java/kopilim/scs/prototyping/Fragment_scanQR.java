package kopilim.scs.prototyping;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_scanQR extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SurfaceView cameraView;
    private TextView textResult, textUnderline;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    final int RequestCameraPermissionID = 1001;

    public Fragment_scanQR() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_scan_qr, container, false);
        cameraView = (SurfaceView) v.findViewById(R.id.cameraView);
        textResult = (TextView) v.findViewById(R.id.textView);
        textUnderline = (TextView) v.findViewById(R.id.resultScanQr);

        String udata = "Result:";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        textUnderline.setText(content);


        barcodeDetector = new BarcodeDetector.Builder(v.getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(v.getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 640).build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {

                    textResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textResult.setText(qrcodes.valueAt(0).displayValue);
                            showResultDialogue(qrcodes.valueAt(0).displayValue);

                        }
                    });

                }//End if Statement
            }
        });


        return v;
    }

    /***************************************************************************************
     Display Message from Scanner
     ***********************************************************************************8***/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Scanning Error. Please try Again");
                alertDialog.show();
            } else {
                //show dialogue with result
                showResultDialogue(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //method to construct dialogue with scan results
    public void showResultDialogue(final String result) {

        final AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);

        } else {
            builder = new AlertDialog.Builder(getContext());
        }

        //Stop the scanner
        barcodeDetector.release();


        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Successfully Scan QR Code")
                .setMessage("Result --->  " + result)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        //Click ok and Back to Main Page
                    }
                })
                .setNegativeButton("SCAN AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                                PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        try {
                            cameraSource.start(cameraView.getHolder());
                            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                                @Override
                                public void surfaceCreated(SurfaceHolder holder) {
                                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) !=
                                            PackageManager.PERMISSION_GRANTED) {
                                        //Request permission
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID);
                                        return;
                                    }
                                    try {
                                        cameraSource.start(cameraView.getHolder());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                                }

                                @Override
                                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                                    cameraSource.stop();

                                }
                            });

                            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                                @Override
                                public void release() {

                                }

                                @Override
                                public void receiveDetections(Detector.Detections<Barcode> detections) {
                                    final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                                    if (qrcodes.size() != 0) {

                                        textResult.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Create vibrate
                                                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                                                vibrator.vibrate(1000);
                                                textResult.setText(qrcodes.valueAt(0).displayValue);
                                                showResultDialogue(qrcodes.valueAt(0).displayValue);

                                            }
                                        });

                                    }//End if Statement
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).setNeutralButton("STOP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cameraSource.stop();
                Toast.makeText(getContext(), "Camera is Stop",
                        Toast.LENGTH_SHORT).show();

            }
        }).create().show();

    }

    //------------------------------------------------------------------------------
    //
    //                  Below are System Auto Generated Method
    //
    //------------------------------------------------------------------------------


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}// End of Class