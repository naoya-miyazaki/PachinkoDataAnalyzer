package pachinko.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBUtil {

    private static final String DATASOURCE_NAME = "java:comp/env/jdbc/pachinkoDB";

    // データベース接続を取得する
    public static Connection getConnection() throws SQLException {
        try {
            // データソースを取得
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(DATASOURCE_NAME);

            if (ds == null) {
                throw new SQLException("データソースが見つかりません: " + DATASOURCE_NAME);
            }

            // 接続を取得
            return ds.getConnection();
        } catch (NamingException e) {
            // NamingException を SQLException に変換して詳細なメッセージを追加
            throw new SQLException("データソースの取得に失敗しました: " + DATASOURCE_NAME, e);
        }
    }
}
