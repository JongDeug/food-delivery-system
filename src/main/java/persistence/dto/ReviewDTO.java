package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private int pk;
    private int orderFk;
    private String memberId;
    private String reviewContent;
    private String starRating;
    private String storeManagerId;
    private String replyToReviewContent;
}
