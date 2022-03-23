package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.Menu;
import com.example.demo.src.store.model.MenuCategory;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public List<GetStoreRes> getStoresByCategory(String category){
        System.out.println("category = " + category);
        String StoreListByCateQuery="SELECT Store.store_idx\n" +
                "From Store\n" +
                "Inner join (SELECT sc.store_idx,c.category_name\n" +
                "\t\t\tFrom category c\n" +
                "            INNER JOIN store_cate sc \n" +
                "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                "Where category_name= ? ";

        String findCateParam=category;

        List<Integer> storeIdxList=this.jdbcTemplate.query(StoreListByCateQuery,
                (rs,rowNum)->{
                   return rs.getInt("store_idx");
                },findCateParam);
        System.out.println("storeIdxList.size() = " + storeIdxList.size());
        List<GetStoreRes> storeListbyCate=new ArrayList<>();
        for(int idx:storeIdxList){
            System.out.println("idx = " + idx);
            String findCateQuery="SELECT scc.category_name\n" +
                    "From Store\n" +
                    "Inner join (SELECT sc.store_idx,c.category_name\n" +
                    "\t\t\tFrom category c\n" +
                    "            INNER JOIN store_cate sc \n" +
                    "            ON c.category_id=sc.cate_idx) scc ON Store.store_idx=scc.store_idx\n" +
                    "Where Store.store_idx= ?";

            List<String> storeCateList=this.jdbcTemplate.query(
                    findCateQuery,
                    (rs,rowNum)->{
                        String cname=rs.getString("category_name");
                        System.out.println("cname = " + cname);
                        return cname;
                    },
                    idx
            );


            String findMenuCategoryListQuery = "SELECT DISTINCT menu_category_name\n" +
                        "From menu\n" +
                       "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                       "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                       "WHERE S.store_idx= ?";

            List<String> menuCategoryList= this.jdbcTemplate.query(findMenuCategoryListQuery,
                       (rs,rowNum)->rs.getString(1),idx);

            List<MenuCategory> menuListSortedByCategory = new ArrayList<>();

            for(String menuCateName:menuCategoryList) {
                //식당에서 설정한 메뉴 카테고리 별로 포함된 메뉴 리스트 출력
                System.out.println("menuCateName = " + menuCateName);
                String findMenuListQuery = "SELECT menu_idx,menu_name,menu_details\n" +
                        "From menu\n" +
                        "INNER JOIN (SELECT store_idx FROM Store) S ON menu.store_idx=S.store_idx\n" +
                        "INNER JOIN menu_category mc ON  menu.menu_category_idx=mc.menu_category_idx\n" +
                        "WHERE S.store_idx= ? and mc.menu_category_name=?";
                Object[]menuParams={idx,menuCateName};
                List<Menu> menuList = this.jdbcTemplate.query(findMenuListQuery,
                        (rs, rowNum) -> new Menu(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3)
                        ), menuParams);

                menuListSortedByCategory.add(new MenuCategory(menuCateName,menuList));

            }

            String findImageURLListQuery="SELECT store_img_url FROM store_img Where store_idx=?";
            List<String> imageURLList=this.jdbcTemplate.query(findImageURLListQuery,
                    (rs,rowNum)->rs.getString(1),idx);

            String findStoreInfoQuery="SELECT store_name,store_min_order,CONCAT_WS(' ',store_siNm,store_sggNm,store_emdNm,store_streetNm,store_detailNm) AS store_address,store_phone,\n" +
                    "store_owner,store_reg_num,store_buisness_hour,store_info,store_owner_note,store_join_date,store_delivery_fee\n" +
                    "From Store\n" +
                    "Where Store.store_idx=? ";

            GetStoreRes getStoreRes=this.jdbcTemplate.queryForObject(findStoreInfoQuery,
                    (rs,rowNum)->{
                        String store_name=rs.getString("store_name");
                        int store_min_order=rs.getInt("store_min_order");
                        String store_address=rs.getString("store_address");
                        int store_phone=rs.getInt("store_phone");
                        String store_owner=rs.getString("store_owner");
                        String store_reg_num=rs.getString("store_reg_num");
                        String store_buisness_hour=rs.getString("store_buisness_hour");
                        String store_info=rs.getString("store_info");
                        String store_owner_note=rs.getString("store_owner_note");
                        Timestamp store_join_date=rs.getTimestamp("store_join_date");
                        System.out.println("store_join_date = " + store_join_date);
                        int store_delivery_fee=rs.getInt("store_delivery_fee");
                        GetStoreRes gSR=new GetStoreRes(idx,store_name,store_min_order,store_address,store_phone,store_owner,
                                store_reg_num,store_buisness_hour,store_info,store_owner_note,store_join_date,store_delivery_fee,storeCateList,imageURLList,menuListSortedByCategory);
                        return gSR;

                    },idx);
            storeListbyCate.add(getStoreRes);


        }
        return storeListbyCate;

    }
}
