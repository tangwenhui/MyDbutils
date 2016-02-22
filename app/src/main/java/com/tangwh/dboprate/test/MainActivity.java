package com.tangwh.dboprate.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.administrator.dboperate.R;

/**
 * Created by Administrator on 2015/10/22 0022.
 */
public class MainActivity extends AppCompatActivity{
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        User user= new User("tangwenhui","846765179@qq.com",new Date(),122);
//        UserDaoImpl userDao = new UserDaoImpl(this,"md.db");
//        userDao.save(user);
        et = (EditText) findViewById(R.id.et);


    }

}
