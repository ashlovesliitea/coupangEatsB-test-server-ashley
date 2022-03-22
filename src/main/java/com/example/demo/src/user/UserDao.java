package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("user_idx"),
                        rs.getString("user_id"),
                        rs.getString("user_pw"),
                        rs.getString("user_name"),
                        rs.getString("user_phone"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from User where user_id =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("user_idx"),
                        rs.getString("user_id"),
                        rs.getString("user_pw"),
                        rs.getString("user_name"),
                        rs.getString("user_phone")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from User where user_idx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("user_idx"),
                        rs.getString("user_id"),
                        rs.getString("user_pw"),
                        rs.getString("user_name"),
                        rs.getString("user_phone")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String lastInsertedIdxQuery="select max(user_idx) from User";
        int lastInsertedid= this.jdbcTemplate.queryForObject(lastInsertedIdxQuery,int.class);
        System.out.println("lastInsertedid = " + lastInsertedid);
        Object[] createUserParams= new Object[]{lastInsertedid+1,postUserReq.getUser_id(),postUserReq.getUser_pw(),postUserReq.getUser_name()
                            ,postUserReq.getUser_phone()};
        String createUserQuery = "insert into User(user_idx,user_id, user_pw,user_name,user_phone) VALUES (?,?,?,?,?)";

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        return this.jdbcTemplate.queryForObject(lastInsertedIdxQuery,int.class);
    }

    public int checkUserId(String userId){
        String checkEmailQuery = "select exists(select user_id from User where user_id = ?)";
        String checkEmailParams = userId;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set user_name = ? where user_idx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUser_name(), patchUserReq.getUser_idx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select user_idx,user_id,user_pw,user_name,user_phone from User where user_id= ?";
        String getPwdParams = postLoginReq.getUser_id();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("user_idx"),
                        rs.getString("user_id"),
                        rs.getString("user_pw"),
                        rs.getString("user_name"),
                        rs.getString("user_phone")
                ),
                getPwdParams
                );

    }


}
