package kopilim.scs.prototyping;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Fragment_createColorQR extends Fragment {

    public static final int RESULT_OK = -1;
    private static final int SELECT_PHOTO = 100;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    private boolean i1 = false, i2 = false, i3 = false, i4 = false;
    private boolean i5 = false, i6 = false, i7 = false, i8 = false;
    private boolean b1 = false, b2 = false, b3 = false, b4 = false;
    private boolean b5 = false, b6 = false, b7 = false, b8 = false;
    private Button generateColorBtn;
    private String finalText = null;
    private saveImageUtils saveImageUtils = new saveImageUtils();


    public Fragment_createColorQR() {
        // Required empty public constructor
    }

    /**********************************************

     Check Same Height and Width
     *********************************************/
    public static boolean sameHeight(Integer checkHeight[]) {
        Set<Integer> s = new HashSet<>(Arrays.asList(checkHeight));

        //if all element are asme, size of HasSet will be 1.
        //Else.. get false
        return (s.size() == 1);
    }

    public static boolean sameWidth(Integer checkWidth[]) {
        Set<Integer> s = new HashSet<>(Arrays.asList(checkWidth));

        return (s.size() == 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_color_qr, container, false);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Caution");
        alert.setMessage("Please read the rule before you generating Color QR Code");
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do ntg
            }
        });
        alert.create().show();

        img1 = (ImageView) v.findViewById(R.id.image1);
        img2 = (ImageView) v.findViewById(R.id.image2);
        img3 = (ImageView) v.findViewById(R.id.image3);
        img4 = (ImageView) v.findViewById(R.id.image4);
        img5 = (ImageView) v.findViewById(R.id.image5);
        img6 = (ImageView) v.findViewById(R.id.image6);
        img7 = (ImageView) v.findViewById(R.id.image7);
        img8 = (ImageView) v.findViewById(R.id.image8);


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i1 = true;
                //i2 = false; i3 = false; i4 = false;
                //i5 = false; i6 = false; i7 = false; i8 = false;
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                openImg();
                i2 = true;
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i3 = true;
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i4 = true;
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i5 = true;
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i6 = true;
            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i7 = true;
            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                i8 = true;
            }
        });


        generateColorBtn = (Button) v.findViewById(R.id.colorQRBtn);

        generateColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try{
                Bitmap bitmap1 = ((BitmapDrawable) img1.getDrawable()).getBitmap();
                Bitmap bitmap2 = ((BitmapDrawable) img2.getDrawable()).getBitmap();
                Bitmap bitmap3 = ((BitmapDrawable) img3.getDrawable()).getBitmap();
                Bitmap bitmap4 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                Bitmap bitmap5 = ((BitmapDrawable) img5.getDrawable()).getBitmap();
                Bitmap bitmap6 = ((BitmapDrawable) img6.getDrawable()).getBitmap();
                Bitmap bitmap7 = ((BitmapDrawable) img7.getDrawable()).getBitmap();
                Bitmap bitmap8 = ((BitmapDrawable) img8.getDrawable()).getBitmap();

                Integer[] checkHeight = {
                        bitmap1.getHeight(), bitmap2.getHeight(), bitmap3.getHeight(),
                        bitmap4.getHeight(), bitmap5.getHeight(), bitmap6.getHeight(),
                        bitmap7.getHeight(), bitmap8.getHeight()
                };

                Integer[] checkWidth = {
                        bitmap1.getWidth(), bitmap2.getWidth(), bitmap3.getWidth(),
                        bitmap4.getWidth(), bitmap5.getWidth(), bitmap6.getWidth(),
                        bitmap7.getWidth(), bitmap8.getWidth(),
                };

                if ((sameWidth(checkWidth)) && (sameHeight(checkHeight))) {
                    //if return is true...
                    //Get the Data from the QR COde

                    //1st Image
                    Bitmap selectedImage1 = ((BitmapDrawable) img1.getDrawable()).getBitmap();
                    int width = selectedImage1.getWidth();
                    int height = selectedImage1.getHeight();
                    int pixel[] = new int[width * height];
                    selectedImage1.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source1 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap1 = new BinaryBitmap(new HybridBinarizer(source1));
                    Reader reader1 = new MultiFormatReader();
                    Result result1 = null;

                    //2nd Image
                    Bitmap selectedImage2 = ((BitmapDrawable) img2.getDrawable()).getBitmap();
                    selectedImage2.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source2 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap2 = new BinaryBitmap(new HybridBinarizer(source2));
                    Reader reader2 = new MultiFormatReader();
                    Result result2 = null;

                    Bitmap selectedImage3 = ((BitmapDrawable) img3.getDrawable()).getBitmap();
                    selectedImage3.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source3 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap3 = new BinaryBitmap(new HybridBinarizer(source3));
                    Reader reader3 = new MultiFormatReader();
                    Result result3 = null;

                    Bitmap selectedImage4 = ((BitmapDrawable) img4.getDrawable()).getBitmap();
                    selectedImage4.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source4 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap4 = new BinaryBitmap(new HybridBinarizer(source4));
                    Reader reader4 = new MultiFormatReader();
                    Result result4 = null;

                    Bitmap selectedImage5 = ((BitmapDrawable) img5.getDrawable()).getBitmap();
                    selectedImage5.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source5 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap5 = new BinaryBitmap(new HybridBinarizer(source5));
                    Reader reader5 = new MultiFormatReader();
                    Result result5 = null;

                    Bitmap selectedImage6 = ((BitmapDrawable) img6.getDrawable()).getBitmap();
                    selectedImage6.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source6 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap6 = new BinaryBitmap(new HybridBinarizer(source6));
                    Reader reader6 = new MultiFormatReader();
                    Result result6 = null;

                    Bitmap selectedImage7 = ((BitmapDrawable) img7.getDrawable()).getBitmap();
                    selectedImage7.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source7 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap7 = new BinaryBitmap(new HybridBinarizer(source7));
                    Reader reader7 = new MultiFormatReader();
                    Result result7 = null;

                    Bitmap selectedImage8 = ((BitmapDrawable) img8.getDrawable()).getBitmap();
                    selectedImage8.getPixels(pixel, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source8 = new RGBLuminanceSource(width,
                            height, pixel);
                    BinaryBitmap binaryBitmap8 = new BinaryBitmap(new HybridBinarizer(source8));
                    Reader reader8 = new MultiFormatReader();
                    Result result8 = null;

                    try {
                        result1 = reader1.decode(binaryBitmap1);
                        result2 = reader2.decode(binaryBitmap2);
                        result3 = reader3.decode(binaryBitmap3);
                        result4 = reader4.decode(binaryBitmap4);
                        result5 = reader5.decode(binaryBitmap5);
                        result6 = reader6.decode(binaryBitmap6);
                        result7 = reader7.decode(binaryBitmap7);
                        result8 = reader8.decode(binaryBitmap8);

                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (ChecksumException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }

                    //Get all the result
                    String text1 = result1.getText();
                    String text2 = result2.getText();
                    String text3 = result3.getText();
                    String text4 = result4.getText();
                    String text5 = result5.getText();
                    String text6 = result6.getText();
                    String text7 = result7.getText();
                    String text8 = result8.getText();


                    //FInal Result -- COmbine all the Text
                    finalText = "1st QR Code Content\n" + text1
                            + "\n\n2nd QR Code Content\n" + text2
                            + "\n\n3rd QR Code Content\n" + text3
                            + "\n\n4th QR Code Content\n" + text4
                            + "\n\n5th QR Code Content\n" + text5
                            + "\n\n6th QR Code Content\n" + text6
                            + "\n\n7th QR Code Content\n" + text7
                            + "\n\n8th QR Code Content\n" + text8
                    ;


                    //******************** END OF Decoding 8 BitMap*******************


                    String[][] intArray1 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray2 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray3 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray4 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray5 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray6 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray7 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    String[][] intArray8 = new String[bitmap1.getHeight()][bitmap1.getWidth()];
                    //Size of array

                    //Get the HEX STRING from all pixel and all QR CODE
                    //And Store in 2D array
                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        // ------ This is X
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            // ---------- This is Y
                            intArray1[a][b] = Integer.toHexString(bitmap1.getPixel(a, b));
                            intArray2[a][b] = Integer.toHexString(bitmap2.getPixel(a, b));
                            intArray3[a][b] = Integer.toHexString(bitmap3.getPixel(a, b));
                            intArray4[a][b] = Integer.toHexString(bitmap4.getPixel(a, b));
                            intArray5[a][b] = Integer.toHexString(bitmap5.getPixel(a, b));
                            intArray6[a][b] = Integer.toHexString(bitmap6.getPixel(a, b));
                            intArray7[a][b] = Integer.toHexString(bitmap7.getPixel(a, b));
                            intArray8[a][b] = Integer.toHexString(bitmap8.getPixel(a, b));
                        }
                    }//end of Nested FOR


                    //After get the HEX STRING from the pixel of all QR CODE
                    //Convert HEX STRING to 1 and 0
                    //Then store 1 and 0 value to arrayInput
                    int[][] arrayInput1 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray1[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput1[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray1[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput1[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray1[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput1[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }// end of nested for loops
                    // Return 1 and 0 value

                    int[][] arrayInput2 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray2[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput2[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray2[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput2[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray2[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput2[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput3 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray3[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput3[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray3[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput3[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray3[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput3[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput4 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray4[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput4[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray4[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput4[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray4[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput4[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput5 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray5[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput5[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray5[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput5[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray5[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput5[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput6 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray6[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput6[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray6[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput6[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray6[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput6[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput7 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray7[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput7[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray7[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput7[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray7[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput7[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    int[][] arrayInput8 = new int[bitmap1.getWidth()][bitmap1.getHeight()];

                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight() - 1; b++) {
                            if (intArray8[a][b].equalsIgnoreCase("ffffffff")) {
                                //This is ALL white
                                arrayInput8[a][b] = Integer.parseInt(String.valueOf(1));
                            } else if (intArray8[a][b].equalsIgnoreCase("ff000000")) {
                                //This is black
                                arrayInput8[a][b] = Integer.parseInt(String.valueOf(0));
                            } else if (intArray8[a][b].equalsIgnoreCase("00000000")) {
                                //THis is black
                                arrayInput8[a][b] = Integer.parseInt(String.valueOf(0));
                            }
                        }
                    }

                    /////////////////////////////////////////
                    //////////////////////// DONE ////


                    //*** Note
                    // *** arrayInput1 is having 1 and 0 value.
                    // *** *** list1 having 1 and 0

                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int a = 0; a < bitmap1.getWidth(); a++) {
                        for (int b = 0; b < bitmap1.getHeight(); b++) {
                            String a1 = String.valueOf(arrayInput1[a][b]);
                            String a2 = String.valueOf(arrayInput2[a][b]);
                            String a3 = String.valueOf(arrayInput3[a][b]);
                            String a4 = String.valueOf(arrayInput4[a][b]);
                            String a5 = String.valueOf(arrayInput5[a][b]);
                            String a6 = String.valueOf(arrayInput6[a][b]);
                            String a7 = String.valueOf(arrayInput7[a][b]);
                            String a8 = String.valueOf(arrayInput8[a][b]);
                            arrayList.add(a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8); // Store 1110001 into ArrayList
                        }
                    }


                    String[] hexArrayRed = new String[arrayList.size()];
                    String[] hexArrayGreen = new String[arrayList.size()];
                    String[] hexArrayBlue = new String[arrayList.size()];
                    String[] hexArrayColor = new String[arrayList.size()];
                    arrayList.toArray(hexArrayRed);
                    arrayList.toArray(hexArrayGreen);
                    arrayList.toArray(hexArrayBlue);
                    arrayList.toArray(hexArrayColor);
                    // hexArray is carry 11100 data.


                    //
                    //              RED COLOR QR
                    //

                    for (int a = 0; a < hexArrayRed.length; a++) {
                        int dec = Integer.parseInt(String.valueOf(arrayList.get(a)), 2);
                        String hexString = Integer.toString(dec, 16);
                        while (hexString.length() < 2) {
                            hexString = "0" + hexString;
                        }
                        String head = "#ff";
                        String behind = "0000";
                        hexArrayRed[a] = head + hexString + behind;
                            /*
                                    Red Hexadecimal Value --> #ff _ _ 0000
                             */
                    }

                    for (int a = 0; a < hexArrayGreen.length; a++) {
                        int dec = Integer.parseInt(String.valueOf(arrayList.get(a)), 2);
                        String hexString = Integer.toString(dec, 16);
                        while (hexString.length() < 2) {
                            hexString = "0" + hexString;
                        }
                        String head = "#ff";
                        String behind = "00";
                        hexArrayGreen[a] = head + behind + hexString + behind;
                            /*
                                    Green Hexadecimal Value --> #ff00 _ _ 00
                             */
                    }


                    for (int a = 0; a < hexArrayBlue.length; a++) {
                        int dec = Integer.parseInt(String.valueOf(arrayList.get(a)), 2);
                        String hexString = Integer.toString(dec, 16);
                        while (hexString.length() < 2) {
                            hexString = "0" + hexString;
                        }
                        String head = "#ff";
                        String alpha = "0000";
                        hexArrayBlue[a] = head + alpha + hexString;
                            /*
                                    Blue Hexadecimal Value --> #ff0000 _ _
                             */
                    }

                    for (int a = 0; a < hexArrayColor.length; a++) {

                        int dec = Integer.parseInt(String.valueOf(arrayList.get(a)), 2);
                        String hexString = Integer.toString(dec, 16);
                        while (hexString.length() < 2) {
                            hexString = "f" + hexString;
                        }

                        Random random = new Random();
                        String alphaHex = "0123456789ABCDEF";

                        StringBuilder stringBuilder2 = new StringBuilder();
                        StringBuilder stringBuilder3 = new StringBuilder();
                        StringBuilder stringBuilder4 = new StringBuilder();

                        while (stringBuilder2.length() < 2
                                || stringBuilder3.length() < 2 || stringBuilder4.length() < 2) {

                            int index2 = (int) (random.nextFloat() * alphaHex.length());
                            stringBuilder2.append(alphaHex.charAt(index2));

                            int index3 = (int) (random.nextFloat() * alphaHex.length());
                            stringBuilder3.append(alphaHex.charAt(index3));

                            int index4 = (int) (random.nextFloat() * alphaHex.length());
                            stringBuilder4.append(alphaHex.charAt(index4));
                        }
                        String tranparent3 = stringBuilder3.toString();
                        String tranparent4 = stringBuilder4.toString();

                        String font = "#77";
                        hexArrayColor[a] = font + hexString + tranparent3 + tranparent4;
                            /*
                                    Color Hexadecimal Value --> #_ _ _ _ _ _ _ _
                             */
                    }


                    QRCodeWriter qwRed = new QRCodeWriter();
                    try {
                        HashMap<EncodeHintType, Object> hints = new HashMap<>();
                        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                        hints.put(EncodeHintType.MARGIN, 2);

                        BitMatrix matrix = qwRed.encode(finalText,
                                BarcodeFormat.QR_CODE,
                                bitmap1.getWidth(),
                                bitmap1.getHeight(),
                                hints);

                        //START OF RED
                        final Bitmap newBitmapRed = Bitmap.createBitmap(
                                bitmap1.getWidth(),
                                bitmap1.getHeight(),
                                Bitmap.Config.ARGB_8888
                        );
                        int counter1 = 0;
                        for (int a = 0; a < bitmap1.getWidth(); a++) {
                            for (int b = 0; b < bitmap1.getHeight(); b++) {
                                //int c = 0;
                                int[] color = new int[hexArrayRed.length];
                                color[counter1] = Color.parseColor(hexArrayRed[counter1]);
                                int d = matrix.get(a, b) ? color[counter1] : Color.WHITE;
                                newBitmapRed.setPixel(a, b, d);
                                counter1++;
                            }
                        }
                        //END OF RED


                        //START OF GREEN
                        final Bitmap newBitmapGreen = Bitmap.createBitmap(
                                bitmap1.getWidth(),
                                bitmap1.getHeight(),
                                Bitmap.Config.ARGB_8888
                        );
                        int counter2 = 0;
                        for (int a = 0; a < bitmap1.getWidth(); a++) {
                            for (int b = 0; b < bitmap1.getHeight(); b++) {
                                //int c = 0;
                                int[] color = new int[hexArrayGreen.length];
                                color[counter2] = Color.parseColor(hexArrayGreen[counter2]);
                                int d = matrix.get(a, b) ? color[counter2] : Color.WHITE;
                                newBitmapGreen.setPixel(a, b, d);
                                counter2++;
                            }
                        }
                        //END OF GREEN

                        //START OF BLUE
                        final Bitmap newBitmapBlue = Bitmap.createBitmap(
                                bitmap1.getWidth(),
                                bitmap1.getHeight(),
                                Bitmap.Config.ARGB_8888
                        );

                        int counter3 = 0;
                        for (int a = 0; a < bitmap1.getWidth(); a++) {
                            for (int b = 0; b < bitmap1.getHeight(); b++) {
                                //int c = 0;
                                int[] color = new int[hexArrayBlue.length];
                                color[counter3] = Color.parseColor(hexArrayBlue[counter3]);
                                int d = matrix.get(a, b) ? color[counter3] : Color.WHITE;
                                newBitmapBlue.setPixel(a, b, d);
                                counter3++;
                            }
                        }
                        //END OF BLUE

                        //START OF COLORFUL
                        final Bitmap newBitmapColor = Bitmap.createBitmap(
                                bitmap1.getWidth(),
                                bitmap1.getHeight(),
                                Bitmap.Config.ARGB_8888
                        );
                        int counter4 = 0;
                        for (int a = 0; a < bitmap1.getWidth(); a++) {
                            for (int b = 0; b < bitmap1.getHeight(); b++) {
                                //int c = 0;
                                int[] color = new int[hexArrayColor.length];
                                color[counter4] = Color.parseColor(hexArrayColor[counter4]);
                                int d = matrix.get(a, b) ? color[counter4] : Color.WHITE;
                                newBitmapColor.setPixel(a, b, d);
                                counter4++;
                            }
                        }
                        //END OF COLORFUL

                        Random random = new Random();
                        int redRandom = random.nextInt(10000);
                        int greenRandom = random.nextInt(10000);
                        int blueRandom = random.nextInt(10000);
                        int colorfulRandom = random.nextInt(10000);

                        final String fnameRed = "Red" + redRandom;
                        final String fnameGreen = "Green" + greenRandom;
                        final String fnameBlue = "Blue" + blueRandom;
                        final String fnameColorful = "Colorful" + colorfulRandom;

                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Processing");
                        progressDialog.setMessage("Printing Color");
                        progressDialog.setIcon(R.mipmap.ic_launcher);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle("Select Color").setItems(new CharSequence[]{"Red", "Green", "Blue", "Colorful"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                if (newBitmapRed != null) {
                                                    saveImageUtils.insertImage(getActivity().getContentResolver(),
                                                            newBitmapRed, fnameRed);
                                                    //SaveImage(newBitmapRed, fnameRed);
                                                    progressDialog.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            intentToResult(newBitmapRed);
                                                            Toast.makeText(getContext(),
                                                                    "Color QR Code Saved.",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    Toast.makeText(getContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                            case 1:
                                                if (newBitmapGreen != null) {
                                                    saveImageUtils.insertImage(getActivity().getContentResolver(),
                                                            newBitmapGreen, fnameGreen);
                                                    //SaveImage(newBitmapGreen, fnameGreen);
                                                    progressDialog.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            intentToResult(newBitmapGreen);
                                                            Toast.makeText(getContext(),
                                                                    "Color QR Code Saved.",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    Toast.makeText(getContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                            case 2:
                                                if (newBitmapBlue != null) {
                                                    saveImageUtils.insertImage(getActivity().getContentResolver(),
                                                            newBitmapBlue, fnameBlue);
                                                    //SaveImage(newBitmapBlue, fnameBlue);
                                                    progressDialog.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            intentToResult(newBitmapBlue);
                                                            Toast.makeText(getContext(),
                                                                    "Color QR Code Saved.",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    Toast.makeText(getContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                            case 3:
                                                if (newBitmapColor != null) {
                                                    saveImageUtils.insertImage(getActivity().getContentResolver(),
                                                            newBitmapColor, fnameColorful);
                                                    //SaveImage(newBitmapColor, fnameColorful);
                                                    progressDialog.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            intentToResult(newBitmapColor);
                                                            Toast.makeText(getContext(),
                                                                    "Color QR Code Saved.",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }, 2000);

                                                } else {
                                                    Toast.makeText(getContext(), "Something is Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                        }
                                    }
                                });
                        builder.create().show();

                        //SaveImage(newBitmapRed,fnameRed);
                        //SaveImage(newBitmapGreen,fnameGreen);
                        //SaveImage(newBitmapBlue,fnameBlue);
                        //SaveImage(newBitmapColor, fnameColorful);


                    } catch (WriterException e) {

                    }
                } else {
                    //If return false...
                    displayAlertDialog();
                }
            }
        });

        return v;
    }//end of OnCreateView

    @Override
    public void onPause() {
        super.onPause();
        buttonVisibility();
    }

    @Override
    public void onResume() {
        super.onResume();
        buttonVisibility();
    }

    @Override
    public void onStart() {
        super.onStart();
        buttonVisibility();
    }

    public void intentToResult(Bitmap bitmap) {
        fragment_result_colorQR f = new fragment_result_colorQR();
        if (bitmap != null) {
            CachePot.getInstance().push(bitmap);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewPage, f, null).addToBackStack(null);
            fragmentTransaction.commit();

        } else {
            Toast.makeText(getContext(), "Error Passing Image", Toast.LENGTH_SHORT).show();
        }
    }

    //Open Image Gallery
    public void openImg() {
        Intent photoPic = new Intent(Intent.ACTION_PICK);
        photoPic.setType("image/*");
        startActivityForResult(photoPic, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = null;

                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);

                        if (i1 == true) {
                            img1.setImageURI(selectedImage);
                            i1 = false;
                            b1 = true;
                            buttonVisibility();
                        }
                        if (i2 == true) {
                            img2.setImageURI(selectedImage);
                            i2 = false;
                            b2 = true;
                            buttonVisibility();
                        }
                        if (i3 == true) {
                            img3.setImageURI(selectedImage);
                            i3 = false;
                            b3 = true;
                            buttonVisibility();
                        }
                        if (i4 == true) {
                            img4.setImageURI(selectedImage);
                            i4 = false;
                            b4 = true;
                            buttonVisibility();
                        }
                        if (i5 == true) {
                            img5.setImageURI(selectedImage);
                            i5 = false;
                            b5 = true;
                            buttonVisibility();
                        }
                        if (i6 == true) {
                            img6.setImageURI(selectedImage);
                            i6 = false;
                            b6 = true;
                            buttonVisibility();
                        }
                        if (i7 == true) {
                            img7.setImageURI(selectedImage);
                            i7 = false;
                            b7 = true;
                            buttonVisibility();
                        }
                        if (i8 == true) {
                            img8.setImageURI(selectedImage);
                            i8 = false;
                            b8 = true;
                            buttonVisibility();
                        }
                        buttonVisibility();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }//end try-catch
                }//end of 1st IF

                else {
                    //If user open gallery but select ntg.
                    //do ntg
                }

        }//end of Switch
    }//end of Method


    // Set button Visibility
    // After User add all 8 number of QR Code
    // It Display the Button

/*
    private void SaveImage(Bitmap finalBitmap, String name) {
        //alertDialog();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/1Saved_Color_QR_Code");
        myDir.mkdirs();
        String fname = name + ".png";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void buttonVisibility() {
        if ((b1 == true) &&
                (b2 == true) &&
                (b3 == true) &&
                (b4 == true) &&
                (b5 == true) &&
                (b6 == true) &&
                (b7 == true) &&
                (b8 == true)
        ) {
            generateColorBtn.setVisibility(View.VISIBLE);
        } else {
            generateColorBtn.setVisibility(View.INVISIBLE);
        }
    }//End of Button Visibility

    public void displayAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Size of QR Code Not Match");
        alertDialog.setMessage("Please make sure all the QR Code is in the SAME Width x Height");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do ntg
            }
        });
        alertDialog.create();
        alertDialog.show();
    }


}
