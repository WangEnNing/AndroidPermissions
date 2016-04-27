package androidpermissions.wen.com.androidpermissions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        String type = getIntent().getStringExtra("type");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(type);

    }
}
