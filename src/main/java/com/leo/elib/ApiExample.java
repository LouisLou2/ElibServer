package com.leo.elib;

import com.leo.elib.entity.dao.Author;
import com.leo.elib.mapper.AuthorMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Data
class Book{
    private String title;
    public Book(){
        this.title = "Hello World";
    }
}

@Getter
@NoArgsConstructor
class AuthorIdReq{
    private int authorId;
}
@RestController
public class ApiExample {
    @Resource
    AuthorMapper authorMapper;
    @GetMapping("/api/author")
    public Author hello(@RequestBody AuthorIdReq req, HttpServletRequest request){
        // var tokenInfo = (TokenInfo)request.getAttribute("tokenInfo");
        return authorMapper.getAuthorById(req.getAuthorId());
    }
}
