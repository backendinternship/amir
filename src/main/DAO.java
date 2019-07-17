package main;

import java.sql.*;

class DAO {
    public static final String RECORDS_TABLE = ConfigReader.getInstance().getFromConfigFile("RECORDS_TABLE", ConfigReader.DB_CONFIG_FILE);
    public static final String VIEW_COUNT_TABLE = ConfigReader.getInstance().getFromConfigFile("VIEW_COUNT_TABLE", ConfigReader.DB_CONFIG_FILE);
    public static final String DATABASE_NAME = ConfigReader.getInstance().getFromConfigFile("DATABASE_NAME", ConfigReader.DB_CONFIG_FILE);
    public static final String ID_COL = ConfigReader.getInstance().getFromConfigFile("ID_COL", ConfigReader.DB_CONFIG_FILE);
    public static final String TITLE_COL = ConfigReader.getInstance().getFromConfigFile("TITLE_COL", ConfigReader.DB_CONFIG_FILE);
    public static final String DESCRIPTION_COL = ConfigReader.getInstance().getFromConfigFile("DESCRIPTION_COL", ConfigReader.DB_CONFIG_FILE);
    public static final String LINK_COL = ConfigReader.getInstance().getFromConfigFile("LINK_COL", ConfigReader.DB_CONFIG_FILE);
    public static final String VIEW_COUNT_COL = ConfigReader.getInstance().getFromConfigFile("VIEW_COUNT_COL", ConfigReader.DB_CONFIG_FILE);

    private static DAO instance = new DAO();
    private Connection connect;

    {
        try {
            connect = DriverManager.getConnection(String.format("jdbc:mysql://localhost/%s?" + "user=%s&password=%s",
                    DATABASE_NAME,
                    ConfigReader.getInstance().getFromConfigFile("username", ConfigReader.DB_CONFIG_FILE),
                    ConfigReader.getInstance().getFromConfigFile("password", ConfigReader.DB_CONFIG_FILE)));
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private DAO() {
    }

    public static DAO getInstance() {
        return instance;
    }

    void insertRecord(Record record) {
        try (PreparedStatement ps = connect.prepareStatement(
                String.format("insert ignore into  %s.%s values (?, ?, ?, ?)", DATABASE_NAME, RECORDS_TABLE)
        )) {
            ps.setString(1, String.valueOf(record.id));
            ps.setString(2, record.title);
            ps.setString(3, record.des);
            ps.setString(4, record.link);
            ps.execute();

            for (int i = 0; i < record.viewCount; i++) {
                incrementViewCount(record.id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    void deleteRecord(int id) {
        try (PreparedStatement ps = connect.prepareStatement(
                String.format("delete from %s.%s where %s=?", DATABASE_NAME, RECORDS_TABLE, ID_COL)
        )) {
            ps.setString(1, String.valueOf(id));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    Record getRecord(int id) {
        try (PreparedStatement ps = connect.prepareStatement(
                String.format("select * from %s.%s left join %s.%s on %s.%s = %s.%s where %s.%s = ?", DATABASE_NAME
                        , RECORDS_TABLE, DATABASE_NAME, VIEW_COUNT_TABLE, RECORDS_TABLE, ID_COL, VIEW_COUNT_TABLE, ID_COL
                        , RECORDS_TABLE, ID_COL)
        )) {
            ps.setString(1, String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String title = rs.getString(TITLE_COL);
                String des = rs.getString(DESCRIPTION_COL);
                String link = rs.getString(LINK_COL);
                int vc = rs.getString(VIEW_COUNT_COL) == null ? 0 : Integer.valueOf(rs.getString(VIEW_COUNT_COL));

                return new Record(id, title, des, link, vc);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;//lie
        }
    }

    void incrementViewCount(int id) {
        try (PreparedStatement ps = connect.prepareStatement(
                String.format("Update %s.%s Set %s = %s + 1 Where %s = ?", DATABASE_NAME, VIEW_COUNT_TABLE, VIEW_COUNT_COL
                        , VIEW_COUNT_COL, ID_COL)
        )) {
            ps.setString(1, String.valueOf(id));
            if (ps.executeUpdate() == 0) {
                try (PreparedStatement ps1 = connect.prepareStatement(
                        String.format("insert into  %s.%s values (?, ?)", DATABASE_NAME, VIEW_COUNT_TABLE)
                )) {
                    ps1.setString(1, String.valueOf(id));
                    ps1.setString(2, "1");
                    ps1.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
