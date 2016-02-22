package com.tangwh.dboprate.dao.base;

import java.util.List;

/**
 * Created by Administrator on 2015/10/22 0022.
 */
public interface  IDaoSupport<T> {
    boolean save(T t);
    int delete(T t);
    long update(T t);
    List<T> findAll();
}
