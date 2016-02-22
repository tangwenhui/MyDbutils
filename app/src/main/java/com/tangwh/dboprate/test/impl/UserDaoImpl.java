package com.tangwh.dboprate.test.impl;

import android.content.Context;

import com.tangwh.dboprate.dao.base.DaoSupportImpl;
import com.tangwh.dboprate.test.User;

/**
 * Created by Administrator on 2015/10/22 0022.
 */
public class UserDaoImpl extends DaoSupportImpl<User>{
    public UserDaoImpl(Context context,String dbName) {
        super(context,dbName);
    }
}
