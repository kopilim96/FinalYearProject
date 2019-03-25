package kopilim.scs.prototyping;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class fragment_decode extends Fragment {
    private static final int SELECT_PHOTO = 100;
    private ImageButton insertImg;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    private Button clearBtn;
    private boolean click = false;
    private AlertDialog.Builder alertDialog;
    private saveImageUtils saveImageUtils = new saveImageUtils();


    public fragment_decode() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_decode, container, false);

        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Please read the Rule before performing Decode Function. Otherwise, it would cause your phone in Crush.");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do ntg
            }
        });
        alertDialog.create().show();


        img1 = (ImageView) v.findViewById(R.id.decodeImg1);
        img2 = (ImageView) v.findViewById(R.id.decodeImg2);
        img3 = (ImageView) v.findViewById(R.id.decodeImg3);
        img4 = (ImageView) v.findViewById(R.id.decodeImg4);
        img5 = (ImageView) v.findViewById(R.id.decodeImg5);
        img6 = (ImageView) v.findViewById(R.id.decodeImg6);
        img7 = (ImageView) v.findViewById(R.id.decodeImg7);
        img8 = (ImageView) v.findViewById(R.id.decodeImg8);

        setHasOptionsMenu(true);
        registerForContextMenu(img1);
        registerForContextMenu(img2);
        registerForContextMenu(img3);
        registerForContextMenu(img4);
        registerForContextMenu(img5);
        registerForContextMenu(img6);
        registerForContextMenu(img7);
        registerForContextMenu(img8);

        img1.buildDrawingCache(true);
        img2.buildDrawingCache(true);
        img3.buildDrawingCache(true);
        img4.buildDrawingCache(true);
        img5.buildDrawingCache(true);
        img6.buildDrawingCache(true);
        img7.buildDrawingCache(true);
        img8.buildDrawingCache(true);


        insertImg = (ImageButton) v.findViewById(R.id.decodeInsertImg);
        insertImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImg();
                click = true;
            }
        });

        onClickEvent();
        longClickEvent();


        clearBtn = (Button) v.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertImg.setImageBitmap(null);
                img1.setImageBitmap(null);
                img2.setImageBitmap(null);
                img3.setImageBitmap(null);
                img4.setImageBitmap(null);
                img5.setImageBitmap(null);
                img6.setImageBitmap(null);
                img7.setImageBitmap(null);
                img8.setImageBitmap(null);
                Toast.makeText(getContext(), "Clear", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.decode_optional_click_list, menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveImage:
                img1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img2.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img3.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img4.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img4.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img5.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img5.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img6.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img6.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img7.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img7.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                img8.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img8.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

*/
    public void onClickEvent() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img1.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img1.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img2.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img2.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img3.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img3.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img4.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img4.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img5.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img5.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img6.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img6.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img7.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img7.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img8.getDrawable() == null) {
                    //do ntg
                } else {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) img8.getDrawable();
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
                        if (result.getText() == null) {
                            Toast.makeText(getContext(), "Empty Data QR Code", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Data: " + result.getText(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void longClickEvent() {
        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();

                return false;
            }
        });

        img2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();

                return false;
            }
        });

        img3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();

                return false;
            }
        });

        img4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();

                return false;
            }
        });

        img5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();
                return false;
            }
        });

        img6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();
                return false;
            }
        });

        img7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();
                return false;
            }
        });

        img8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Save Image");
                alert.setMessage("Are you sure want to save the Image ?");
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Random random = new Random();
                        int x = random.nextInt(1000000);
                        saveImageUtils.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(x));
                        //SaveImage(bitmap);
                        Toast.makeText(getContext(), "Image Saved to Device", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do ntg
                    }
                }).create().show();
                return false;
            }
        });
    }

    //Open Image Gallery
    public void openImg() {
        Intent photoPic = new Intent(Intent.ACTION_PICK);
        photoPic.setType("image/*");
        startActivityForResult(photoPic, SELECT_PHOTO);
    }
    /*
    private void SaveImage(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/1Saved_QR_Code");
        myDir.mkdirs();
        Random random = new Random();
        int x = random.nextInt(100000);
        String fname = x +".png";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(), "Save Image to Device Successful",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void btnVisibility(Boolean click) {
        if (click == true) {
            clearBtn.setVisibility(View.VISIBLE);
        } else {
            clearBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //doing some uri parsing
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        //getting the image
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                        //decoding bitmap
                        Bitmap bMap = BitmapFactory.decodeStream(imageStream);
                        insertImg.setImageURI(selectedImage);

                        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                        // copy pixel data from the Bitmap into the 'intArray' array
                        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
                                bMap.getHeight());

                        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
                                bMap.getHeight(), intArray);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Reader reader = new MultiFormatReader();// use this otherwise
                        // ChecksumException

                        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
                        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

                        Result result = reader.decode(bitmap, decodeHints);

                        if (result.getText() == null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Error");
                            alert.setMessage("Error of Getting Data. Please make sure the Image is QR Code with Data.");
                            alert.setIcon(R.mipmap.ic_launcher);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do ntg
                                }
                            });
                            alert.create().show();
                        } else {
                            String text = result.getText();
                            String[] p1 = text.split("1st QR Code Content");
                            String[] p2 = p1[1].split("2nd QR Code Content");
                            String text1 = p2[0];
                            String[] p3 = p2[1].split("3rd QR Code Content");
                            String text2 = p3[0];
                            String[] p4 = p3[1].split("4th QR Code Content");
                            String text3 = p4[0];
                            String[] p5 = p4[1].split("5th QR Code Content");
                            String text4 = p5[0];
                            String[] p6 = p5[1].split("6th QR Code Content");
                            String text5 = p6[0];
                            String[] p7 = p6[1].split("7th QR Code Content");
                            String text6 = p7[0];
                            String[] p8 = p7[1].split("8th QR Code Content");
                            String text7 = p8[0];
                            String text8 = p8[1];

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                            //Text Content put in the QR COde and generated with X = 100 Y = 100
                            BitMatrix bitMatrix1 = multiFormatWriter.encode(text1, BarcodeFormat.QR_CODE, 100, 100);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix1); //create QR Code
                            img1.setImageBitmap(bitmap1);
                            img1.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix2 = multiFormatWriter.encode(text2, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap2 = barcodeEncoder.createBitmap(bitMatrix2); //create QR Code
                            img2.setImageBitmap(bitmap2);
                            img2.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix3 = multiFormatWriter.encode(text3, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap3 = barcodeEncoder.createBitmap(bitMatrix3); //create QR Code
                            img3.setImageBitmap(bitmap3);
                            img3.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix4 = multiFormatWriter.encode(text4, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap4 = barcodeEncoder.createBitmap(bitMatrix4); //create QR Code
                            img4.setImageBitmap(bitmap4);
                            img4.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix5 = multiFormatWriter.encode(text5, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap5 = barcodeEncoder.createBitmap(bitMatrix5); //create QR Code
                            img5.setImageBitmap(bitmap5);
                            img5.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix6 = multiFormatWriter.encode(text6, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap6 = barcodeEncoder.createBitmap(bitMatrix6); //create QR Code
                            img6.setImageBitmap(bitmap6);
                            img6.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix7 = multiFormatWriter.encode(text7, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap7 = barcodeEncoder.createBitmap(bitMatrix7); //create QR Code
                            img7.setImageBitmap(bitmap7);
                            img7.setScaleType(ImageView.ScaleType.FIT_XY);

                            BitMatrix bitMatrix8 = multiFormatWriter.encode(text8, BarcodeFormat.QR_CODE, 100, 100);
                            Bitmap bitmap8 = barcodeEncoder.createBitmap(bitMatrix8); //create QR Code
                            img8.setImageBitmap(bitmap8);
                            img8.setScaleType(ImageView.ScaleType.FIT_XY);


                            click = true;
                            btnVisibility(click);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getContext(), "File not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        Toast.makeText(getContext(), "Nothing to be Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Unable to Perform Decode", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    //If click back when open imge gallery.
                    //Return ntg
                }
        }//end of Switch
    }//End of ActivityResult


}//End of Class
