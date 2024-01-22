package persistence.dao;

import network.clientAPI.StoreManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.MemberDTO;
import persistence.dto.MenuDTO;
import persistence.dto.OrderDTO;
import persistence.dto.StoreDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDAO {
    private final SqlSessionFactory sqlSessionFactory;

    private static MemberDAO instance;

    static {
        instance = new MemberDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    private MemberDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static MemberDAO getInstance() {
        return instance;
    }

    /**
     * 등록
     */
    public void registerSignUp(MemberDTO memberDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.insert("mapper.MemberMapper.registerSignUp", memberDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 응답 조회
     */
    public List<MemberDTO> responseStoreManagerRead() {
        SqlSession session = sqlSessionFactory.openSession();
        List<MemberDTO> memberDTOS = null;
        try {
            memberDTOS = session.selectList("mapper.MemberMapper.responseStoreManagerRead");
        } finally {
            session.close();
        }
        return memberDTOS;
    }

    /**
     * 응답
     */
    public void responseStoreManager(int pk, String response) {
        SqlSession session = sqlSessionFactory.openSession(true);
        Map<String, Object> map = new HashMap<>();
        map.put("pk", pk);
        map.put("response", response);
        try {
            session.update("mapper.MemberMapper.responseStoreManager", map);
        } finally {
            session.close();
        }
    }

    /**
     * 조회
     */
    public MemberDTO readMyAccountInfo(int pk) {
        SqlSession session = sqlSessionFactory.openSession();
        MemberDTO memberDTO = null;
        try {
            memberDTO = session.selectOne("mapper.MemberMapper.readMyAccountInfo", pk);
        } finally {
            session.close();
        }
        return memberDTO;
    }

    public List<OrderDTO> readMyOrderInfo(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> orderDTOS = null;
        try {
            orderDTOS = session.selectList("mapper.OrderMapper.readMyOrderInfo", memberId);
        } finally {
            session.close();
        }
        return orderDTOS;
    }

    public List<OrderDTO> readMyOrderResInfo(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> orderDTOS = null;
        try {
            orderDTOS = session.selectList("mapper.OrderMapper.readMyOrderResInfo", memberId);
        } finally {
            session.close();
        }
        return orderDTOS;
    }

    public List<MemberDTO> readStoreManagerInfo() {
        SqlSession session = sqlSessionFactory.openSession();
        List<MemberDTO> memberDTOS = null;
        try {
            memberDTOS = session.selectList("mapper.MemberMapper.readStoreManagerInfo");
        } finally {
            session.close();
        }
        return memberDTOS;
    }

    public List<MemberDTO> readClientInfo() {
        SqlSession session = sqlSessionFactory.openSession();
        List<MemberDTO> memberDTOS = null;
        try {
            memberDTOS = session.selectList("mapper.MemberMapper.readClientInfo");
        } finally {
            session.close();
        }
        return memberDTOS;
    }

    public MemberDTO readAccount(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        MemberDTO memberDTO = null;
        try {
            memberDTO = session.selectOne("mapper.MemberMapper.readAccount", memberId);
        } finally {
            session.close();
        }
        return memberDTO;
    }

    public MemberDTO readStoreManagerAccount(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        MemberDTO memberDTO = null;
        try {
            memberDTO = session.selectOne("mapper.MemberMapper.readStoreManagerAccount", memberId);
        } finally {
            session.close();
        }
        return memberDTO;
    }

    public MemberDTO readAdminAccount(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        MemberDTO memberDTO = null;
        try {
            memberDTO = session.selectOne("mapper.MemberMapper.readAdminAccount", memberId);
        } finally {
            session.close();
        }
        return memberDTO;
    }

    /**
     * 수정
     */
    public void updateProfile(MemberDTO memberDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.update("mapper.MemberMapper.updateProfile", memberDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 그 외 데이터 처리
     */
    public boolean checkDuplicate(String memberId) {
        SqlSession session = sqlSessionFactory.openSession();
        boolean check = false;
        String id = null;
        try {
            id = session.selectOne("mapper.MemberMapper.checkDuplicate", memberId);
            if (id != null) {
                if (id.equals(memberId)) {
                    check = true;
                } else {
                    check = false;
                }
            } else {
                check = false;
            }
        } finally {
            session.close();
        }
        return check;
    }
}
