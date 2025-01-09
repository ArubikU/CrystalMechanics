package dev.arubiku.libs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.h2.jdbcx.JdbcConnectionPool;

import com.google.gson.JsonObject;

import dev.arubiku.libs.blocker.FeaturedBlock.BlockType;

public class H2CrystalData extends CrystalData {
  private final JdbcConnectionPool connectionPool;
  private final JavaPlugin plugin;

  public H2CrystalData(JavaPlugin plugin, String url, String user, String password) {
    this.plugin = plugin;
    this.connectionPool = JdbcConnectionPool.create(url, user, password);
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
    try (Connection conn = connectionPool.getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      handleSQLException(e);
    }
  }

  private <T> T fetchSingle(String sql, PreparedStatementSetter setter, ResultSetExtractor<T> extractor) {
    try (Connection conn = connectionPool.getConnection();
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
    try (Connection conn = connectionPool.getConnection();
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

  private CompletableFuture<Void> executeUpdateWithValuesAsync(String sql, PreparedStatementSetter setter) {
    return CompletableFuture.runAsync(() -> {
      try (Connection conn = connectionPool.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
        setter.setValues(pstmt);
        pstmt.executeUpdate();
      } catch (SQLException e) {
        handleSQLException(e);
      }
    }, task -> Bukkit.getScheduler().runTaskAsynchronously(plugin, task));
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
    executeUpdateWithValuesAsync(
        "MERGE INTO blocks (id, x, y, z, world, type) VALUES (?, ?, ?, ?, ?, ?)",
        ps -> {
          ps.setInt(1, block.getId());
          ps.setInt(2, block.getX());
          ps.setInt(3, block.getY());
          ps.setInt(4, block.getZ());
          ps.setString(5, block.getWorld());
          ps.setString(6, block.getType().toString());
        });
  }

  @Override
  public void saveMetadataToDatabase(CrystalMetadata metadata) {
    executeUpdateWithValuesAsync(
        "MERGE INTO metadata (id, data) VALUES (?, ?)",
        ps -> {
          ps.setInt(1, metadata.getId());
          ps.setString(2, metadata.getDataAsString());
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
    CompletableFuture<Void> removeBlock = executeUpdateWithValuesAsync(
        "DELETE FROM blocks WHERE id = ?",
        ps -> ps.setInt(1, id));
    CompletableFuture<Void> removeMetadata = executeUpdateWithValuesAsync(
        "DELETE FROM metadata WHERE id = ?",
        ps -> ps.setInt(1, id));
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

  @FunctionalInterface
  private interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
  }

  @FunctionalInterface
  private interface PreparedStatementSetter {
    void setValues(PreparedStatement ps) throws SQLException;
  }
}
