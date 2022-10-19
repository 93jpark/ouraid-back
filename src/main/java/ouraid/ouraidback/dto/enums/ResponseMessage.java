package ouraid.ouraidback.dto.enums;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_CHAR = "캐릭 생성 성공";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String CREATED_USER_FAIL = "회원 가입 실패";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";

    public static final String RESOURCE_AVAILABLE = "자원 사용 가능";

    public static final String DUPLICATE_RES = "중복된 리소스";
    public static final String DUPLICATE_NICKNAME = "중복된 닉네임";
    public static final String DUPLICATE_EMAIL = "중복된 이메일";
    public static final String DUPLICATE_CHAR_NAME = "중복된 캐릭명";
    public static final String DUPLICATE_GUILD_NAME = "중복된 길드명";
    public static final String DUPLICATE_COM_NAME = "중복된 연합명";
}
