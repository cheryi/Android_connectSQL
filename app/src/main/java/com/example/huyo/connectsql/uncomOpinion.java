package com.example.huyo.connectsql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class uncomOpinion extends AppCompatActivity {

    TextView lowPS;
    TextView eyeS;
    TextView footS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncom_opinion);
        initialView();
        givesuggestion();
    }

    public void initialView(){
        /*lowpTitle=findViewById(R.id.lowpTitle);
        eyeTitle=findViewById(R.id.eyeTitle);
        footTitle=findViewById(R.id.footTitle);*/
        lowPS=findViewById(R.id.lowpS);
        eyeS=findViewById(R.id.eyeS);
        footS=findViewById(R.id.footS);
    }

    public void givesuggestion(){
        lowPS.setText("suggestion");
        eyeS.setText("suggestion");
        footS.setText("suggestion");
    }
}
