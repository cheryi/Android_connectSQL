package com.example.huyo.connectsql;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=findViewById(R.id.txt);
        new Thread(new Runnable() {
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
                        String sql_select = "select count(*) from record;";
                        ResultSet totalcount = stmt.executeQuery(sql_select);
                        Log.d("SQLstate","rs successful");
                        totalcount.next();
                        int lasti = totalcount.getInt(1);
                        Log.d("last id ",Integer.toString(lasti));
                        String sql_insert = "insert into record (id, time) values ("+Integer.toString(lasti+1)+",'"+timeStr+"');"; //要执行的sql语句
                        Boolean rs = stmt.execute(sql_insert);//insert, update等更新要用execute
                        //Log.d("rsSTAT",rs.toString());
                    }else{
                        Log.d("调试","连接失败");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(MainActivity.this, MENUindex.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 5000);


    }
}
