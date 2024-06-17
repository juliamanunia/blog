package com.example.blog.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.blog.controller.dto.ArticleDto;
import com.example.blog.controller.dto.CreateArticleRequest;
import com.example.blog.controller.dto.UpdateArticleRequest;

public interface ArticleService {

	List<ArticleDto> getArticles();

	Optional<ArticleDto> getArticle(Long id);

	List<ArticleDto> getArticlesBeforeDate(LocalDate beforeDate);

	ArticleDto createArticle(CreateArticleRequest request);

	Optional<ArticleDto> updateArticle(Long id, UpdateArticleRequest request);

	void deleteArticle(Long id);
}
