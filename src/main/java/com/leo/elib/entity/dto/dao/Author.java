package com.leo.elib.entity.dto.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = "author")
public class Author {
    public static final List<String> worthSearchFriendlyFields = List.of(
        "name", "desc"
    );
    @Id
    @Field(type = FieldType.Integer, index = false)
    @JsonProperty("author_id")
    private int authorId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String desc;

    @Field(type = FieldType.Integer, index = false)
    @JsonProperty("book_count")
    private int bookCount; // 书籍总数
}