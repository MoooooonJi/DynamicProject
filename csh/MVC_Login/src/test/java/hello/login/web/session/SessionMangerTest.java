package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

public class SessionMangerTest {
    SessionManger sessionManger =new SessionManger();

    @Test
    void sessionTest(){

        //세션 생성
        MockHttpServletResponse response= new MockHttpServletResponse(); //test 하기 위해서
        Member member=new Member();
        sessionManger.createSession(member,response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request= new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result= sessionManger.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManger.expire(request);
        Object expired = sessionManger.getSession(request);
        Assertions.assertThat(expired).isNull();

    }
}
