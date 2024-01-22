package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.MenuDTO;
import persistence.dto.OptionDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionDAO {

    private final SqlSessionFactory sqlSessionFactory;

    private static final OptionDAO instance;

    static {
        instance = new OptionDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }
    private OptionDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static OptionDAO getInstance(){
        return instance;
    }
    /**
     * 등록
     */
    public void registerMenuOption(OptionDTO optionDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try{
            session.insert("mapper.OptionMapper.registerMenuOption", optionDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 조회
     */
    public List<OptionDTO> readStoreMenuOptionInfo(int fk) {
        SqlSession session = sqlSessionFactory.openSession();
        List<OptionDTO> optionDTOS = null;
        try {
            optionDTOS = session.selectList("mapper.OptionMapper.readStoreMenuOptionInfo", fk);
        } finally {
            session.close();
        }
        return optionDTOS;
    }

    /**
     * 응답 조회
     */
    public List<OptionDTO> responseMenuOptionRead(){
        SqlSession session = sqlSessionFactory.openSession();
        List<OptionDTO> optionDTOS = null;
        try{
            optionDTOS = session.selectList("mapper.OptionMapper.responseMenuOptionRead");
        }finally {
            session.close();
        }
        return optionDTOS;
    }

    /**
     * 응답
     */
    public void responseMenuOption(int pk, String response){
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk",pk);
        map.put("response", response);
        try{
            session.update("mapper.OptionMapper.responseMenuOption", map);
        }finally {
            session.close();
        }
    }



    /**
     * 그 외 처리
     */
    public int optionPrice(int storeFk, String optionName) {
        SqlSession session = sqlSessionFactory.openSession();
        int price = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("storeFk", storeFk);
        map.put("optionName", optionName);
        try {
            price = session.selectOne("mapper.OptionMapper.optionPrice", map);
        } finally {
            session.close();
        }
        return price;
    }
}
