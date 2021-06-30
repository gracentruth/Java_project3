import java.sql.*;

public class Main {


    //테이블 생성
    final String CREATETBL = "create table if not exists score (" +
            "id integer primary key autoincrement, " +
            "name text not null, " +
            "kor integer default 0," +
            "eng integer default 0," +
            "math integer default 0," +
            "regdate datetime DEFAULT (datetime('now','localtime'))" +
            ")";
    //if not exists : table이 없으면 만들어라

    public static void main(String args[]){
        Main main = new Main();
        main.sqliteFunc();
    }

    void sqliteFunc(){
        Connection conn = null; //database와 연결 클래스 (항상 필요)
        Statement stmt = null; //sql 실행 클래스 (항상 필요)
        PreparedStatement pstmt = null;

        // sqlite jdbc 클래스 확인
        try {
            Class.forName("org.sqlite.JDBC"); //JDBC: java data base connectivity(?)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            //test.db 데이터베이스 연결(없으면 생성) connection을 만든다
            conn = DriverManager.getConnection("jdbc:sqlite:test.db"); //project내에 생성

            //테이블 생성 : SQL문 실행
            stmt = conn.createStatement();
            stmt.execute(CREATETBL);//이 쿼리를 실행해 !

            //데이터 추가 : Statment 사용
            String sql = "insert into score (name, kor, eng, math) values ('홍길동', 100, 80, 90)";//<- 데이타 추가 쿼리
            stmt.execute(sql);//위의 쿼리(sql)를 실행해 !

            //데이터 추가 : PreparedStatement 사용
            String sql2 = "insert into score (name, kor, eng, math) values(?,?,?,?)"; //실행할 때마다 값만 바뀌도록 함
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, "김한동");//parameterIndex: 물음표 인덱스
            pstmt.setInt(2, 90);
            pstmt.setInt(3, 100);
            pstmt.setInt(4, 80);
            pstmt.executeUpdate();// 완성된 걸 실행해라 !

            //데이터 조회 : SQL문 실행
            ResultSet rs = stmt.executeQuery("select * from score");
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int kor = rs.getInt("kor");
                int eng = rs.getInt("eng");
                int math = rs.getInt("math");
                String regdate = rs.getString("regdate");
                System.out.println(id + " " + name + " " + kor + " " + eng + " " + math + " " + regdate);
            }//받아와서 한줄씩 읽는다
            rs.close();
            pstmt.close();
            stmt.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
