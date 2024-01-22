package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.ReviewDTO;

import java.util.List;

public class ReviewDAO {
    private final SqlSessionFactory sqlSessionFactory;

    private static final ReviewDAO instance;

    static {
        instance = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    private ReviewDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static ReviewDAO getInstance() {
        return instance;
    }

    /**
     * 등록
     */
    public void registerReviewAndStarRating(ReviewDTO reviewDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.insert("mapper.ReviewMapper.registerReviewAndStarRating", reviewDTO);
        } finally {
            session.close();
        }
    }

    public void registerReplyToReview(ReviewDTO reviewDTO) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.update("mapper.ReviewMapper.registerReplyToReview", reviewDTO);
        } finally {
            session.close();
        }
    }

    /**
     * 조회
     */
    // 자신의 store review 전체 조회함.
    public List<ReviewDTO> readStoreReview(int fk) {
        SqlSession session = sqlSessionFactory.openSession();
        List<ReviewDTO> reviewDTOS = null;
        try {
            reviewDTOS = session.selectList("mapper.ReviewMapper.readStoreReview", fk);
        } finally {
            session.close();
        }
        return reviewDTOS;
    }

    public ReviewDTO readReviewFk(int fk) {
        SqlSession session = sqlSessionFactory.openSession();
        ReviewDTO reviewDTO = null;
        try {
           reviewDTO = session.selectOne("mapper.ReviewMapper.readReviewFk", fk);
        } finally {
            session.close();
        }
        return reviewDTO;
    }

    public ReviewDTO readReviewPk(int pk) {
        SqlSession session = sqlSessionFactory.openSession();
        ReviewDTO reviewDTO = null;
        try {
            reviewDTO = session.selectOne("mapper.ReviewMapper.readReviewPk", pk);
        } finally {
            session.close();
        }
        return reviewDTO;
    }
}