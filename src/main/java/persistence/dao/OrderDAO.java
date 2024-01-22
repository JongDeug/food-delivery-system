package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.OrderDTO;
import persistence.dto.StoreStatisticDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    private final SqlSessionFactory sqlSessionFactory;

    private static final OrderDAO instance;

    static {
        instance = new OrderDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }
    private OrderDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static OrderDAO getInstance(){
        return instance;
    }
    /**
     * 등록
     */
    public void registerOrder(OrderDTO dto) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.insert("mapper.OrderMapper.registerOrder", dto);
        } finally {
            session.close();
        }
    }

    /**
     * 응답 취소 등록
     */
    public void registerCancleOrder(int pk){
        SqlSession session = sqlSessionFactory.openSession(true);
        try{
            session.update("mapper.OrderMapper.registerCancleOrder", pk);
        }finally {
            session.close();
        }
    }

    /**
     * 응답 조회
     */
    public List<OrderDTO> responseCancleOrderRead(int storeFk){
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> orderDTOS = null;
        try{
            orderDTOS = session.selectList("mapper.OrderMapper.responseCancleOrderRead", storeFk);
        }finally {
            session.close();
        }
        return orderDTOS;
    }

    public List<OrderDTO> responseOrderRead(int storeFk){
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> orderDTOS = null;
        try{
            orderDTOS = session.selectList("mapper.OrderMapper.responseOrderRead", storeFk);
        }finally {
            session.close();
        }
        return orderDTOS;
    }

    /**
     * 응답
     */
    public void responseCancleOrder(int pk, String response){
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk", pk);
        map.put("response", response);
        try{
            session.update("mapper.OrderMapper.responseCancleOrder", map);
        }finally {
            session.close();
        }
    }

    public void responseOrder(int pk, String response){
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk", pk);
        map.put("response", response);
        try{
            session.update("mapper.OrderMapper.responseOrder", map);
        }finally {
            session.close();
        }
    }

    /**
     * 조회
     */
    public List<OrderDTO> readOrderInfoAll(int fk) {
        List<OrderDTO> orderDTOS = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            orderDTOS = session.selectList("mapper.OrderMapper.readOrderInfoAll", fk);
        } finally {
            session.close();
        }
        return orderDTOS;
    }

    public OrderDTO readOrderInfo(int pk) {
        OrderDTO orderDTO= null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            orderDTO = session.selectOne("mapper.OrderMapper.readOrderInfo", pk);
        } finally {
            session.close();
        }
        return orderDTO;
    }

    /**
     * 수정
     */
    public void updateStatus(int pk, String originalStatus, String setOrderStatus) {
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("setOrderStatus", setOrderStatus);
        map.put("originalStatus", originalStatus);
        map.put("pk", pk);
        try {
            session.update("mapper.OrderMapper.updateStatus", map);
        } finally {
            session.close();
        }
    }
}
