package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static com.example.demo.utils.calculateDistanceByKilometer.*;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreRes> getStoresByCategory(int user_address_idx,String category){
        System.out.println("user_address_idx = " + user_address_idx);
        System.out.println("category = "+category);
        String StoreListByCateQuery="SELECT Store.store_idx,Store.store_lng,Store.store_lat\n" +
                "From Store\n" +
                "Inner join (SELECT sc.store_idx,c.category_name\n" +
                "\t\t\tFrom category c\n" +
                "            INNER JOIN store_cate sc \n" +
                "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                "Where category_name= ? ";

        String findCateParam=category;
        Object[] findCateParams={category,user_address_idx,user_address_idx};
        List<StoreLocation> storeIdxList=this.jdbcTemplate.query(StoreListByCateQuery,
                (rs,rowNum)->new StoreLocation(
                   rs.getInt(1),
                   rs.getDouble(2),
                rs.getDouble(3))
                ,findCateParam);

        List<StoreDist>storeIdxFilteredByLocationList=getStoreListFilteredByLocation(storeIdxList,user_address_idx);
        List<GetStoreRes> storeListbyCate=new ArrayList<>();

        for(StoreDist storeDist:storeIdxFilteredByLocationList){
            GetStoreRes getStoreRes=getWholeStoreInfo(storeDist);
            storeListbyCate.add(getStoreRes);

        }
        return storeListbyCate;

    }

    public List<StoreDist> getStoreListFilteredByLocation(List <StoreLocation> storeIdxList, int user_address_idx){
        List<StoreDist>storeIdxListFilteredByLocation=new ArrayList<>();
        for(StoreLocation storeLocation :storeIdxList){

            String findUsrLngQuery="select user_address_lng,user_address_lat from User_Address where user_address_idx=?";
            double[] usrLocation= this.jdbcTemplate.queryForObject(findUsrLngQuery,
                    (rs,rowNum)->{
                        double userLng=rs.getDouble(1);
                        double userLat=rs.getDouble(2);
                        double[] location={userLng,userLat};
                        return location;
                    },user_address_idx);
            double user_lng= usrLocation[0];
            double user_lat= usrLocation[1];

            double dist= distance(user_lat,user_lng, storeLocation.getStore_lat(), storeLocation.getStore_lng());
            System.out.println("dist = " + dist);

            if(dist<10){ //10km 반경에 존재하면 렌더링 리스트에 포함
                storeIdxListFilteredByLocation.add(new StoreDist(storeLocation.getStore_idx(),dist));
            }

        }
        return storeIdxListFilteredByLocation;
    }

    public GetStoreRes getWholeStoreInfo(StoreDist storeDist) {
        int store_idx= storeDist.getStore_idx();
        //식당에 포함된 카테고리 리스트
        String findCateQuery = "SELECT scc.category_name\n" +
                "From Store\n" +
                "Inner join (SELECT sc.store_idx,c.category_name\n" +
                "\t\t\tFrom category c\n" +
                "            INNER JOIN store_cate sc \n" +
                "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                "Where Store.store_idx= ?";

        List<String> storeCateList = this.jdbcTemplate.query(
                findCateQuery,
                (rs, rowNum) -> {
                    String cname = rs.getString("category_name");
                    return cname;
                },
               store_idx
        );


        String findMenuCategoryListQuery = "SELECT DISTINCT menu_category_name\n" +
                "From menu\n" +
                "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                "WHERE S.store_idx= ?";

        List<String> menuCategoryList = this.jdbcTemplate.query(findMenuCategoryListQuery,
                (rs, rowNum) -> rs.getString(1), store_idx);

        List<MenuCategory> menuListSortedByCategory = new ArrayList<>();

        for (String menuCateName : menuCategoryList) {
            //식당에서 설정한 메뉴 카테고리 별로 포함된 메뉴 리스트 출력
            System.out.println("menuCateName = " + menuCateName);
            String findMenuListQuery = "SELECT menu_idx,menu_name,menu_details\n" +
                    "From menu\n" +
                    "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                    "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                    "WHERE S.store_idx= ? and mc.menu_category_name=?";
            Object[] menuParams = {storeDist.getStore_idx(), menuCateName};
            List<Menu> menuList = this.jdbcTemplate.query(findMenuListQuery,
                    (rs, rowNum) -> new Menu(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3)
                    ), menuParams);

            menuListSortedByCategory.add(new MenuCategory(menuCateName, menuList));

        }
        //식당 대표이미지 URL 리스트 출력
        String findImageURLListQuery = "SELECT store_img_url FROM store_img Where store_idx=?";
        List<String> imageURLList = this.jdbcTemplate.query(findImageURLListQuery,
                (rs, rowNum) -> rs.getString(1), store_idx);

        String findStoreInfoQuery = "SELECT store_name,store_min_order,CONCAT_WS(' ',store_siNm,store_sggNm,store_emdNm,store_streetNm,store_detailNm) AS store_address,store_phone,\n" +
                "store_owner,store_reg_num,store_buisness_hour,store_info,store_owner_note,store_join_date,store_delivery_fee,store_lat,store_lng\n" +
                "From Store\n" +
                "Where Store.store_idx=? ";

        GetStoreRes getStoreRes = this.jdbcTemplate.queryForObject(findStoreInfoQuery,
                (rs, rowNum) -> {
                    String store_name = rs.getString("store_name");
                    int store_min_order = rs.getInt("store_min_order");
                    String store_address = rs.getString("store_address");
                    int store_phone = rs.getInt("store_phone");
                    String store_owner = rs.getString("store_owner");
                    String store_reg_num = rs.getString("store_reg_num");
                    String store_buisness_hour = rs.getString("store_buisness_hour");
                    String store_info = rs.getString("store_info");
                    String store_owner_note = rs.getString("store_owner_note");
                    Timestamp store_join_date = rs.getTimestamp("store_join_date");
                    int store_delivery_fee = rs.getInt("store_delivery_fee");
                    double store_lat=rs.getDouble("store_lat");
                    double store_lng=rs.getDouble("store_lng");
                    GetStoreRes gSR = new GetStoreRes(store_idx, store_name, store_min_order, store_address, store_phone, store_owner,
                            store_reg_num, store_buisness_hour, store_info, store_owner_note, store_join_date, store_delivery_fee,
                            store_lat,store_lng,storeDist.getStore_user_dist(),storeCateList, imageURLList, menuListSortedByCategory);
                    return gSR;

                }, store_idx);


        return getStoreRes;
    }
}

