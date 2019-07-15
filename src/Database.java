import java.sql.*;
import java.util.ArrayList;

class Database {
    private static Connection connect;

    static {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost/rss?" + "user=amir&password=amir78");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    static void updateRecords(ArrayList<Record> records) {
        for (Record record : records) {
            if (getRecordById(record.id) == null) {
                try {
                    PreparedStatement ps = connect.prepareStatement("insert into  rss.records values (?, ?, ?, ?, 0)");
                    ps.setString(1, String.valueOf(record.id));
                    ps.setString(2, record.title);
                    ps.setString(3, record.des);
                    ps.setString(4, record.link);
                    ps.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    static Record getRecordById(int id) {
        try {
            PreparedStatement ps = connect.prepareStatement("select * from rss.records where id = ?");
            ps.setString(1, String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String des = rs.getString("des");
                String link = rs.getString("link");
                int vc = Integer.valueOf(rs.getString("vc"));

                return new Record(id, title, des, link, vc);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void incrementViewCount(int id) {
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement("Update rss.records Set vc = vc + 1 Where id = ?");
            ps.setString(1,String.valueOf(id));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
