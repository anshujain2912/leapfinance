package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean validateUser(String userName,String password){
        List list=jdbcTemplate.queryForList("select * from users where username=? and password=?",userName,password);
        if(list.size()==1){
            return true;
        }
        return false;
    }

    public void saveAccessToken(String userName, String accessToken) {
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");
        jdbcTemplate.update("insert into user_token set user_id=?, access_token=?,valid_upto=now()+ INTERVAL 30 MINUTE",id,accessToken);
        return;
    }

    public boolean validateToken(String userName, String accessToken) {
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");
        List list=jdbcTemplate.queryForList("select * from user_token where user_id=? and access_token=? and valid_upto>now()",id,accessToken);
        if(list.size()==1){
            return true;
        }
        return false;
    }
}
