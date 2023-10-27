package hello.login.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import hello.login.domain.member.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static hello.login.jdbc.connection.ConnectionConst.*;

@Component
public class HikarDataSource {

    MemberRepository repository;
    @Bean
    void setting(){
        HikariDataSource dataSource=new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository=new MemberRepository(dataSource);

    }
}
