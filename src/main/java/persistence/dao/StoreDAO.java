package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.StoreDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreDAO {
    private final SqlSessionFactory sqlSessionFactory;

    private static final StoreDAO instance;

    static {
        instance = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }
    private StoreDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static StoreDAO getInstance(){
        return instance;
    }

    /**
     * 등록
     */
    public void registerStore(StoreDTO storeDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.insert("mapper.StoreMapper.registerStore", storeDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 수정
     */
    public void updateBusinessHours(int storePk, LocalTime storeStartTime, LocalTime storeEndTime) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("storePk", storePk);
            map.put("storeStartTime", storeStartTime);
            map.put("storeEndTime", storeEndTime);
            session.insert("mapper.StoreMapper.updateBusinessHours", map);
        } finally {
            session.close();
        }
    }

    /**
     * 응답 조회
     */
    public List<StoreDTO> responseStoreRead(){
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreDTO> storeDTOS = null;
        try{
            storeDTOS = session.selectList("mapper.StoreMapper.responseStoreRead");
        }finally {
            session.close();
        }
        return storeDTOS;
    }

    /**
     * 응답
     */
    public void responseStore(int pk, String response){
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk",pk);
        map.put("response", response);
        try{
            session.update("mapper.StoreMapper.responseStore", map);
        }finally {
            session.close();
        }
    }

    /**
     * 조회
     */
    public List<StoreDTO> readStoreInfoAll(){
         SqlSession session = sqlSessionFactory.openSession();
        List<StoreDTO> storeDTOS = null;
        try {
            storeDTOS = session.selectList("mapper.StoreMapper.readStoreInfoAll");
        } finally {
            session.close();
        }
        return storeDTOS;
    }

    public StoreDTO readStoreInfo(int pk) {
        SqlSession session = sqlSessionFactory.openSession();
        StoreDTO storeDTO = null;
        try {
            storeDTO = session.selectOne("mapper.StoreMapper.readStoreInfo", pk);
        } finally {
            session.close();
        }
        return storeDTO;
    }

    public StoreDTO readStore(int pk){
        StoreDTO storeDTO = new StoreDTO();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            storeDTO = session.selectOne("mapper.StoreMapper.readStoreInfo", pk);
        } finally {
            session.close();
        }
        return storeDTO;
    }
    public List<StoreDTO> readMyStores(int fk){
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreDTO> storeDTOS = null;
        try{
            storeDTOS = session.selectList("mapper.StoreMapper.readMyStores", fk);
        } finally {
            session.close();
        }
        return storeDTOS;
    }
    // 얘는 쓸지도 모름
/*    public int readStoreManagerInfo(int pk) {
        SqlSession session = sqlSessionFactory.openSession();
        int storeManagerFk;
        try {
            storeManagerFk = session.selectOne("mapper.StoreMapper.readStoreManagerInfo", pk);
        } finally {
            session.close();
        }
        return storeManagerFk;
    }*/
}