package dev.arubiku.libs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;

public class MySQLCrystalData extends CrystalData {
  private final HikariDataSource dataSource;

  @FunctionalInterface
  private interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
  }

  @FunctionalInterface
  private interface PreparedStatementSetter {
    void setValues(PreparedStatement ps) throws SQLException;
  }

  public MySQLCrystalData(String url, String user, String password) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setUsername(user);
    config.setPassword(password);
    this.dataSource = new HikariDataSource(config);
    initializeTables();
  }

  private void initializeTables() {
    executeUpdate("CREATE TABLE IF NOT EXISTS blocks (" +
        "id INT PRIMARY KEY, " +
        "x INT, " +
        "y INT, " +
        "z INT, " +
        "world VARCHAR(255), " +
        "type VARCHAR(255))");
    executeUpdate("CREATE TABLE IF NOT EXISTS metadata (id INT PRIMARY KEY, data VARCHAR(255))");
  }

  private void executeUpdate(String sql) {
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      handleSQLException(e);
    }
  }

  private <T> T fetchSingle(String sql, PreparedStatementSetter setter, ResultSetExtractor<T> extractor) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      setter.setValues(pstmt);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return extractor.extractData(rs);
        }
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return null;
  }

  private <T> List<T> fetchAll(String sql, ResultSetExtractor<T> extractor) {
    List<T> results = new ArrayList<>();
    try (Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        results.add(extractor.extractData(rs));
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return results;
  }

  private void executeUpdateWithValues(String sql, PreparedStatementSetter setter) {
    try (Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      setter.setValues(pstmt);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      handleSQLException(e);
    }
  }

  private void handleSQLException(SQLException e) {
    // Log the exception or handle it according to your needs
    e.printStackTrace();
  }

  @Override
  public CrystalBlock fetchBlockFromDatabase(int id) {
    return fetchSingle(
        "SELECT * FROM blocks WHERE id = ?",
        ps -> ps.setInt(1, id),
        rs -> new CrystalBlock(
            rs.getInt("id"),
            rs.getInt("x"),
            rs.getInt("y"),
            rs.getInt("z"),
            rs.getString("world"),
            BlockType.valueOf(rs.getString("type"))));
  }

  @Override
  public CrystalMetadata fetchMetadataFromDatabase(int id) {
    return fetchSingle(
        "SELECT * FROM metadata WHERE id = ?",
        ps -> ps.setInt(1, id),
        rs -> {
          String jsonString = rs.getString("data");
          JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
          return new CrystalMetadata(rs.getInt("id"), jsonObject);
        });
  }

  @Override
  public List<CrystalBlock> fetchAllBlocksFromDatabase() {
    return fetchAll(
        "SELECT * FROM blocks",
        rs -> new CrystalBlock(
            rs.getInt("id"),
            rs.getInt("x"),
            rs.getInt("y"),
            rs.getInt("z"),
            rs.getString("world"),
            BlockType.valueOf(rs.getString("type"))));
  }

  @Override
  public int getIdWhereCordinates(String world, int x, int y, int z) {
    return fetchSingle(
        "SELECT id FROM blocks WHERE world = ? AND x = ? AND y = ? AND z = ?",
        ps -> {
          ps.setString(1, world);
          ps.setInt(2, x);
          ps.setInt(3, y);
          ps.setInt(4, z);
        },
        rs -> rs.getInt("id"));
  }

  @Override
  public List<CrystalMetadata> fetchAllMetadataFromDatabase() {
    return fetchAll(
        "SELECT * FROM metadata",
        rs -> {
          String jsonString = rs.getString("data");
          JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
          return new CrystalMetadata(rs.getInt("id"), jsonObject);
        });
  }

  @Override
  public void saveBlockToDatabase(CrystalBlock block) {
    executeUpdateWithValues(
        "INSERT INTO blocks (id, x, y, z, world, type) VALUES (?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE x=?, y=?, z=?, world=?, type=?",
        ps -> {
          // Insert values
          ps.setInt(1, block.getId());
          ps.setInt(2, block.getX());
          ps.setInt(3, block.getY());
          ps.setInt(4, block.getZ());
          ps.setString(5, block.getWorld());
          ps.setString(6, block.getType().toString());
          // Update values
          ps.setInt(7, block.getX());
          ps.setInt(8, block.getY());
          ps.setInt(9, block.getZ());
          ps.setString(10, block.getWorld());
          ps.setString(11, block.getType().toString());
        });
  }

  @Override
  public void saveMetadataToDatabase(CrystalMetadata metadata) {
    executeUpdateWithValues(
        "INSERT INTO metadata (id, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data=?",
        ps -> {
          String jsonString = metadata.getDataAsString();
          ps.setInt(1, metadata.getId());
          ps.setString(2, jsonString);
          ps.setString(3, jsonString);
        });
  }

  @Override
  public boolean keyExists(int id) {
    return fetchSingle(
        "SELECT COUNT(*) FROM blocks WHERE id = ?",
        ps -> ps.setInt(1, id),
        rs -> rs.getInt(1) > 0);
  }

  @Override
  public void removeKey(int id) {
    executeUpdateWithValues(
        "DELETE FROM blocks WHERE id = ?",
        ps -> ps.setInt(1, id));
    executeUpdateWithValues(
        "DELETE FROM metadata WHERE id = ?",
        ps -> ps.setInt(1, id));
  }
}
