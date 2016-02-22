package com.tangwh.dboprate.test;

import com.tangwh.dboprate.dao.annotation.ColumnName;
import com.tangwh.dboprate.dao.annotation.PrimaryKey;
import com.tangwh.dboprate.dao.annotation.Table;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/16 0016.
 */
@Table(name="s_User")
public class User {
    public User() {
    }

    public User(String name, String email, Date registerDate, int money) {

        this.name = name;
        this.email = email;
        this.registerDate = registerDate;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    @PrimaryKey(autoincreament=true)
    @ColumnName(value="s_id")
    private int id;
    @ColumnName(value = "s_name")
    public String name;
    @ColumnName(value="s_email")
    private String email;
    @ColumnName(value="s_registerDate")
    private Date registerDate;
    @ColumnName(value="s_money")
    private int money;
}
