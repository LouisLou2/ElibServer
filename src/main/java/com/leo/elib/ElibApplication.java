package com.leo.elib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class ElibApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElibApplication.class, args);
    }
}
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ElibApplication {
//
//    public static void main(String[] args) {
//        // 数据库连接配置
//        String jdbcUrl = "jdbc:mysql://localhost:3306/lib_manage";
//        String username = "leo";
//        String password = "lou..200499";
//        Map<String, List<String>> isbnPubMap = new HashMap<>();
//
//        // SQL查询和更新语句
//        String selectSQL = "SELECT isbn, publisher FROM book_info";
//        String updateBookInfoSQl = "UPDATE book_info SET publisher_id = ? WHERE isbn = ?";
//        String insertPublisherSQL = "INSERT INTO publisher (publisher_id, publisher_name, `desc`) VALUES (?, ?, ?)";
//
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
//             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
//            // 查询所有ISBN
//            ResultSet resultSet = selectStatement.executeQuery();
//            while (resultSet.next()) {
//                String isbn = resultSet.getString("isbn");
//                String publisher = resultSet.getString("publisher");
//                if (!isbnPubMap.containsKey(publisher)) {
//                    List<String> lis = new ArrayList<>();
//                    lis.add(isbn);
//                    isbnPubMap.put(publisher, lis);
//                } else {
//                    isbnPubMap.get(publisher).add(isbn);
//                }
//            }
//            int publisherId = 1;
//            for (Map.Entry<String, List<String>> entry : isbnPubMap.entrySet()) {
//                String publisher = entry.getKey();
//                List<String> isbns = entry.getValue();
//                try (PreparedStatement insertPublisherStatement = connection.prepareStatement(insertPublisherSQL)) {
//                    insertPublisherStatement.setInt(1, publisherId);
//                    insertPublisherStatement.setString(2, publisher);
//                    insertPublisherStatement.setString(3, "a good publisher");
//                    insertPublisherStatement.executeUpdate();
//                }
//                for (String isbn : isbns) {
//                    try (PreparedStatement updateBookInfoStatement = connection.prepareStatement(updateBookInfoSQl)) {
//                        updateBookInfoStatement.setInt(1, publisherId);
//                        updateBookInfoStatement.setString(2, isbn);
//                        updateBookInfoStatement.executeUpdate();
//                    }
//                }
//                publisherId++;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}