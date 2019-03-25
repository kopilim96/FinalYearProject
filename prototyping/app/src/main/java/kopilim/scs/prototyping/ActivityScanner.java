package kopilim.scs.prototyping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(ActivityScanner.this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);

    }


    @Override
    public void handleResult(Result result) {
        String textResult = result.getText();
        if(textResult.isEmpty()){
            Toast.makeText(getApplicationContext(), "Empty Data",Toast.LENGTH_SHORT).show();
        }else{
            CachePot.getInstance().push("textResult", textResult);
        }
    }
}
