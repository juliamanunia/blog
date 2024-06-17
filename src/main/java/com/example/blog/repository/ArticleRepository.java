package com.example.blog.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.blog.model.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	List<Article> findAll();

	@Query("from Article article where article.published < :beforeDate")
	List<Article> findAllBeforeDate(@Param("beforeDate") LocalDate beforeDate);
}
