package com.example.huyo.connectsql;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class uncomActivity extends AppCompatActivity {
    Button yesBTN;
    Button noBTN;
    TextView Ques;
    String QueString;
    ArrayList<String> Questions = new ArrayList<String>();
    int quesID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncom);
        yesBTN = findViewById(R.id.yesBTN);
        noBTN = findViewById(R.id.noBTN);
        Ques = findViewById(R.id.uncomQues);

        HandlerThread mthread = new HandlerThread("mthreads_name");
        final Handler h;
        mthread.start();
        h = new Handler(mthread.getLooper());
        h.post(catchQues);

        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.post(outputQues);
            }
        });
        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.post(outputQues);
            }
        });


    }

    private Runnable catchQues = new Runnable() {
        @Override
        public void run(){
            try {
                Class.forName("com.mysql.jdbc.Driver");//动态加载类
                String url = "jdbc:mysql://140.116.247.179:3306/andr";
                //上面语句中 mysql://mysql.lianfangti.top为你的mysql服务器地址 3306为端口号   public是你的数据库名 根据你的实际情况更改
                com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(url, "app", "app");
//使用 DriverManger.getConnection链接数据库  第一个参数为连接地址 第二个参数为用户名 第三个参数为连接密码  返回一个Connection对象
                if(conn!=null){ //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                    Log.d("调试","连接成功");
                    Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                    //get the last record id amd insert current time
                    String sql_select = "select question from uncomques;";
                    ResultSet qes = stmt.executeQuery(sql_select);
                    Log.d("SQLstate","rs successful");
                    while(qes.next()){
                        QueString = qes.getString(1);
                        if (QueString != null)
                            Questions.add(QueString);
                        //Log.d("Q: ",QueString);
                    }
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Ques.setText(Questions.get(0));
                        }
                    });
                }else{
                    Log.d("调试","连接失败");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable outputQues = new Runnable() {
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                public void run()
                {
                    quesID=quesID+1;
                    if (quesID<Questions.size()) {
                        Log.d("quesID", Integer.toString(quesID));
                        Ques.setText(Questions.get(quesID));
                    }
                    else{
                        final Intent mainIntent = new Intent(uncomActivity.this,uncomOpinion.class);
                        uncomActivity.this.startActivity(mainIntent);
                        uncomActivity.this.finish();
                    }

                }
            });
        }
    };

}
