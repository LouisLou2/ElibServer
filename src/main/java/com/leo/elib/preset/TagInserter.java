package com.leo.elib.preset;
import java.sql.*;
import java.util.*;

public class TagInserter {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/lib_manage";
  private static final String USER = "leo";
  private static final String PASS = "lou..200499";

  public static void main(String[] args) {
    List<String> isbnList = getIsbns();
    insertTagsForBooks(isbnList);
  }

  private static List<String> getIsbns() {
    List<String> isbnList = new ArrayList<>();
    String query = "SELECT isbn FROM book_info";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        isbnList.add(rs.getString("isbn"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isbnList;
  }

  private static void insertTagsForBooks(List<String> isbnList) {
    String insertQuery = "INSERT INTO book_tag_cor (isbn, tag_id) VALUES (?, ?)";
    Random random = new Random();

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

      for (String isbn : isbnList) {
        int numberOfTags = random.nextInt(5) + 1; // 1 to 5 tags
        Set<Integer> tagIds = new HashSet<>();

        while (tagIds.size() < numberOfTags) {
          tagIds.add(random.nextInt(45) + 1); // tag_id between 1 and 45
        }

        for (int tagId : tagIds) {
          pstmt.setString(1, isbn);
          pstmt.setInt(2, tagId);
          pstmt.addBatch();
        }
      }
      pstmt.executeBatch(); // Execute batch insert
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}