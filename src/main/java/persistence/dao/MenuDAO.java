package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.MenuDTO;
import persistence.dto.StoreDTO;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDAO {

    private final SqlSessionFactory sqlSessionFactory;

    private static final MenuDAO instance;

    static {
        instance = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    private MenuDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static MenuDAO getInstance() {
        return instance;
    }

    /**
     * 등록
     */
    public void registerMenu(MenuDTO menuDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.insert("mapper.MenuMapper.registerMenu", menuDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 응답 조회
     */
    public List<MenuDTO> responseMenuRead() {
        SqlSession session = sqlSessionFactory.openSession();
        List<MenuDTO> menuDTOS = null;
        try {
            menuDTOS = session.selectList("mapper.MenuMapper.responseMenuRead");
        } finally {
            session.close();
        }
        return menuDTOS;
    }

    /**
     * 응답
     */
    public void responseMenu(int pk, String response){
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk",pk);
        map.put("response", response);
        try{
            session.update("mapper.MenuMapper.responseMenu", map);
        }finally {
            session.close();
        }

    }

    /**
     * 조회
     */
    public List<MenuDTO> readStoreMenuInfo(int fk) {
        SqlSession session = sqlSessionFactory.openSession();
        List<MenuDTO> menuDTOS = null;
        try {
            menuDTOS = session.selectList("mapper.MenuMapper.readStoreMenuInfo", fk);
        } finally {
            session.close();
        }
        return menuDTOS;
    }

    /**
     * 등록
     */
    public void updateAmount(String menuName, String operator) {
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("menuName", menuName);
        map.put("operator", operator);
        try {
            session.update("mapper.MenuMapper.updateAmount", map);
        } finally {
            session.close();
        }
    }

    /**
     * 그 외 처리
     */
    public int menuPrice(int storeFk, String menuName) {
        SqlSession session = sqlSessionFactory.openSession();
        int price = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("storeFk", storeFk);
        map.put("menuName", menuName);
        try {
            price = session.selectOne("mapper.MenuMapper.menuPrice", map);
        } finally {
            session.close();
        }
        return price;
    }

    public int menuAmount(int storeFk, String menuName) {
        SqlSession session = sqlSessionFactory.openSession();
        int amount = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("storeFk", storeFk);
        map.put("menuName", menuName);
        try {
            amount = session.selectOne("mapper.MenuMapper.menuAmount", map);
        } finally {
            session.close();
        }
        return amount;
    }
}
