package com.example.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.blog.controller.dto.ArticleDto;
import com.example.blog.model.Article;

@Mapper
public interface ArticleMapper {

	ArticleMapper ARTICLE = Mappers.getMapper(ArticleMapper.class);

	ArticleDto toDto(Article article);
}
