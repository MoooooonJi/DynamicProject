package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session ==null){
            return "세션 없다.";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={},value={}",name,session.getAttribute(name)));

        log.info("sessionId={}",session.getId());
        log.info("sessionMaxInactiveInterval={}",session.getMaxInactiveInterval()); // 비활성화 최대 시간초
        log.info("sessionCreationTime={}",new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new
                Date(session.getLastAccessedTime())); //마지막으로 접근한시간
        log.info("isNew={}", session.isNew());
        return "세션 출력";
    }
}
