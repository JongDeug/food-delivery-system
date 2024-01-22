package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MemberDTO {
    private int pk;
    private String memberId;
    private String memberPassword;
    private String memberName;
    private int memberAge;
    private String memberTel;
    private String memberRole;
    private String response;
}
