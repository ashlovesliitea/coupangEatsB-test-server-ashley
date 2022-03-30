package com.example.demo.src.user;


import com.example.demo.src.user.model.entity.User;
import com.example.demo.src.user.model.request.*;
import com.example.demo.src.user.model.response.GetAddressRes;
import com.example.demo.src.user.model.response.GetPaymentRes;
import com.example.demo.src.user.model.response.GetUserRes;
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


    public int getUsersById(String user_id) {
        String findUserIdxQuery="select user_idx from User where user_id= ?";
        return this.jdbcTemplate.queryForObject(findUserIdxQuery,
                (rs,rowNum)->rs.getInt(1),user_id);
    }

    public String findUserPhone(int user_idx) {
        String findUserPhoneQuery="select user_phone from User where user_idx=?";
        return this.jdbcTemplate.queryForObject(findUserPhoneQuery,
                (rs,rowNum)->rs.getString(1),user_idx);
    }

    public int createUserAddress(PostAddressReq postAddressReq) {
        String findLastIdQuery="select max(user_address_idx) from User_Address";
        int lastIdx=this.jdbcTemplate.queryForObject(findLastIdQuery,int.class);
        System.out.println("lastIdx = " + lastIdx);
        System.out.println("nullTest = " + postAddressReq.getEmdNm());

        String createUserAddressQuery="insert into User_Address(user_address_idx,user_idx,siNm,sggNm,emdNm,streetNm,detailNm,user_address_lng,user_address_lat)" +
                "Values(?,?,?,?,?,?,?,?,?)";

        Object[] usrAddressParams={lastIdx+1,postAddressReq.getUser_idx(),postAddressReq.getSiNm(),postAddressReq.getSggNm(),postAddressReq.getEmdNm(),
        postAddressReq.getStreetNm(),postAddressReq.getDetailNm(),postAddressReq.getUser_address_lng(),postAddressReq.getUser_address_lat()};
        return this.jdbcTemplate.update(createUserAddressQuery,usrAddressParams);
    }

    public List<GetAddressRes> getUserAddressList(int user_idx) {
        String findUserAddressListQuery="select user_address_idx,CONCAT_WS(' ',siNm,sggNm,emdNm,streetNm,detailNm) AS user_address" +
                "from User_Address where user_idx=?";
        List<GetAddressRes> addressResList=this.jdbcTemplate.query(findUserAddressListQuery,
                (rs,rowNum)->new GetAddressRes(
                        rs.getInt(1),
                        rs.getString(2)
                ),user_idx);
        return addressResList;
    }

    public int deleteUserAddress(int user_address_idx) {
        String deleteAddressQuery="delete from User_Address where user_address_idx=?";
        return this.jdbcTemplate.update(deleteAddressQuery,user_address_idx);
    }

    public int createPayment(PostPaymentReq postPaymentReq) {
        String findLastIdQuery="select max(payment_idx) from User_Payment";
        int lastIdx=this.jdbcTemplate.queryForObject(findLastIdQuery,int.class);

        String createPaymentQuery="insert into User_Payment(payment_idx,user_idx,payment_name) Values(?,?,?)";
        Object[] createPaymentParam={lastIdx+1,postPaymentReq.getUser_idx(),postPaymentReq.getPayment_name()};

        return this.jdbcTemplate.update(createPaymentQuery,createPaymentParam);
    }

    public List<GetPaymentRes> getUserPaymentList(int user_idx) {
        String findPaymentQuery="select payment_idx,payment_name from User_Payment where user_idx=?";
        return this.jdbcTemplate.query(findPaymentQuery,
                (rs,rowNum)->new GetPaymentRes(
                        rs.getInt(1),
                        rs.getString(2)
                ),user_idx
        );
    }

    public int deletePayment(int user_payment_idx) {
        String deletePaymentQuery="delete from User_Payment where user_payment_idx=?";
        return this.jdbcTemplate.update(deletePaymentQuery,user_payment_idx);
    }
}
