package com.example.demo.dao;

import com.example.demo.objects.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.beans.BeanProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ContentDao {
    JdbcTemplate jdbcTemplate;
    @Autowired
    public ContentDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean checkIfUrlPresent(String url){
        List list=jdbcTemplate.queryForList("select * from contents where url=?",url);
        if(list.size()>0){
            return true;
        }
        return false;
    }

    public boolean checkIfUserMappedToUrl(String userName,String url){
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");
        int contentId=jdbcTemplate.queryForRowSet("select content_id from contents where url=?",url).findColumn("content_id");
        List list=jdbcTemplate.queryForList("select * from user_content where user_id=? and content_id=?",id,contentId);
        if(list.size()>0){
            return true;
        }
        return false;
    }

    public void saveContent(String userName,String url,String content){
        KeyHolder holder = new GeneratedKeyHolder();
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            String q = "insert into contents set url=?, content=?";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement s = connection.prepareStatement(q, new String[]{"content_id"});
                s.setString(1,url);
                s.setString(2,content);
                return s;
            }
        };

        jdbcTemplate.update(creator,holder);
        int contentId= holder.getKey().intValue();//jdbcTemplate.queryForRowSet("select content_id from contents where url=?",url).findColumn("content_id");
        jdbcTemplate.update("insert into user_content set user_id=?, content_id=?",id,contentId);
        return;
    }

    public void saveUserToUrl(String userName, String url, String content) {
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");
        int contentId=jdbcTemplate.queryForRowSet("select content_id from contents where url=?",url).findColumn("content_id");
        jdbcTemplate.update("insert into user_content set user_id=?, content_id=?",id,contentId);
        return;
    }

    public List<Content> fetchUserUrls(String userName) {
        int id=jdbcTemplate.queryForRowSet("select user_id from users where username=?",userName).findColumn("user_id");

        return jdbcTemplate.query("select c.url as url,c.content as content from user_content u inner join contents c where u.user_id=?", new Object[]{id}, BeanPropertyRowMapper.newInstance(Content.class));
    }
}
