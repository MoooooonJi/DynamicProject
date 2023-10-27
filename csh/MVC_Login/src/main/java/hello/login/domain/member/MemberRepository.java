package hello.login.domain.member;

import hello.login.jdbc.connection.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;



@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final DataSource dataSource;
    private static Map<Long, Member> store=new HashMap<>();
    private static long sequence = 0L;

//    public Member save(Member member) {
//        member.setId(++sequence);
//        log.info("save: member={}",member);
//        store.put(member.getId(),member);
//        return member;
//    }

    public Member save(Member member) throws SQLException {
        member.setId(UUID.randomUUID().toString());
        String sql = "insert into member(id,loginID, password,name) values(?,?,?,?)";

        Connection con = null;
        PreparedStatement pstmt = null; //DB에 쿼리날리기

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql); // 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비한다
            pstmt.setString(1,member.getId());
            pstmt.setString(2, member.getLoginId()); //파라미터에 대한 값 바인딩
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4,member.getName());

            pstmt.executeUpdate(); //실행
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }


    public Member findById(long id) {
        return store.get(id);
    }
//
//    public Optional<Member> findByLoginId(String loginId) {
////        List<Member> all = findAll();
////        for (Member member : all) {
////            if (member.getLoginId().equals(loginId)) {
////                return Optional.of(member);
////            }
////        }
////        return Optional.empty();
//
//        return findAll().stream()
//                .filter(member -> member.getLoginId().equals(loginId))
//                .findFirst();
//        /*
//        return findAll().stream() // 마치 루프를 도는것이다.
//                .filter(member -> member.getLoginId().equals(loginId))
//                //filter 만족하는 조건만 넘어간다.
//                .findFirst(); //먼저 나오는애를 반환한다.
//                */
//    }


    public Member findByLoginId(String memberId) throws SQLException {
        String sql = "select * from member where loginId=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery(); // 업데이트 아님 애는 찾아주는거

            if (rs.next()) {
                Member member = new Member();
                member.setLoginId(rs.getString("loginId"));
                member.setPassword(rs.getString("password"));
                member.setName(rs.getString("name"));
                log.info("password={}",member.getName());
                return member;
            } else {
                throw new NoSuchElementException("member not found loginId" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

    private void close(Connection cos, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(cos);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        return con;
    }
}
