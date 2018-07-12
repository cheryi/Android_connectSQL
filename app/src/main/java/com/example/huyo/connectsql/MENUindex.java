package com.example.huyo.connectsql;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mysql.jdbc.Connection;

import java.io.IOException;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class MENUindex extends AppCompatActivity {

    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    Button eatBTN;
    Button sleepBTN;
    Button testBTN;
    Button uncomBTN;
    ImageButton recordBTN;

    HandlerThread mthread = new HandlerThread("mthreads_name");
    Handler h,uih;

    ArrayList<String> records = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuindex);
        InitialButton();//initial every button
        uih = new Handler();
        mthread.start();
        h = new Handler(mthread.getLooper());

        recordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h.post(outputRocord);
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        for(int i=0;i<records.size();i++) {
                            Toast toast = Toast.makeText(MENUindex.this,records.get(i) , Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                final Intent mainIntent = new Intent(MENUindex.this,logRecord.class);
                MENUindex.this.startActivity(mainIntent);
            }
        });

        sleepBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(MENUindex.this,sleepActivity.class);
                MENUindex.this.startActivity(mainIntent);
                //MENUindex.this.finish();
            }
        });

        uncomBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(MENUindex.this,uncomActivity.class);
                MENUindex.this.startActivity(mainIntent);
                //MENUindex.this.finish();
            }
        });
    }

    void InitialButton(){
        eatBTN=findViewById(R.id.eatbtn);
        sleepBTN=findViewById(R.id.sleepbtn);
        testBTN=findViewById(R.id.testbtn);
        uncomBTN=findViewById(R.id.uncomforbtn);
        recordBTN=findViewById(R.id.recordbtn);
    }

    private Runnable outputRocord = new Runnable() {
        @Override
        public void run(){
            final Date currentTime = Calendar.getInstance().getTime();
            final String timeStr=currentTime.toString().substring(0,20);
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
                    String sql_select = "select time from record;";
                    ResultSet recordTable = stmt.executeQuery(sql_select);
                    Log.d("SQLstate","rs successful");
                    while(recordTable.next()){
                        //Log.d("record table ", recordTable.getString(1));
                        records.add(recordTable.getString(1));
                    }
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
}
