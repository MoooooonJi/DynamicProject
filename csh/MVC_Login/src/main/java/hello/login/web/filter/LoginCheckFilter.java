package hello.login.web.filter;

import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

@Slf4j
@Component
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login",
            "/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest=(HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작={}",requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}",requestURI);
                HttpSession httpSession=httpRequest.getSession(false);
                if(httpSession==null || httpSession.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                    log.info("미인증 사용자 요청{}",requestURI);

                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return;
                }
            }

            chain.doFilter(request,response);

        }catch(Exception e){
            throw e;
        }finally {
            log.info("인증체크 필터 종료={}", requestURI);

        }


        }
    /**
     * 화이트 리스트의 경우 인증 체크 X
     * */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI);
        // white랑  URI 이랑 매핑이 되는가? 즉 이 메서드의 true 조건이 매핑 안될떄
    }


}
