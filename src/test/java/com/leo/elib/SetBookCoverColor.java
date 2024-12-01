package com.leo.elib;

import com.leo.elib.mapper.BookInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
public class SetBookCoverColor {

  @Resource
  BookInfoMapper bookInfoMapper;

  @Test
  void readAndSetColor(){
    String colorTxtPath = "D:/resources/project_resource/elib_pic/book_cover_color.txt";
    // 第一行是isbn，第二行是颜色
    // 读取文件
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(colorTxtPath));
      String isbn;
      String color;
      while ((isbn = reader.readLine()) != null && (color = reader.readLine()) != null) {
        bookInfoMapper.debug_setColor(isbn, Long.parseLong(color));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
