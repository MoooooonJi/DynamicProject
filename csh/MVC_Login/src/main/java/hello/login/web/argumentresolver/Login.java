package hello.login.web.argumentresolver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //메서드의 파라미터만 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
