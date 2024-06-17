package com.example.blog.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.controller.dto.ArticleDto;
import com.example.blog.controller.dto.CreateArticleRequest;
import com.example.blog.controller.dto.UpdateArticleRequest;
import com.example.blog.service.ArticleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/blog/articles")
public class ArticleController {

	private static final String ARTICLE_NOT_FOUND_MESSAGE = "Article with id = {} not found!";
	private final ArticleService articleService;

	@GetMapping(
			produces = APPLICATION_JSON_VALUE
	)
	public List<ArticleDto> getArticles() {
		log.info("Getting articles");
		List<ArticleDto> articles = articleService.getArticles();
		log.info("Articles = {}", articles);
		return articles;
	}

	@GetMapping(
			value = "/before-date",
			produces = APPLICATION_JSON_VALUE
	)
	public List<ArticleDto> getArticlesBeforeDate(@RequestParam("date") LocalDate beforeDate) {
		log.info("Getting articles published before date = {}", beforeDate);
		List<ArticleDto> articles = articleService.getArticlesBeforeDate(beforeDate);
		log.info("Articles = {}", articles);
		return articles;
	}

	@GetMapping(
			value = "{id}",
			produces = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ArticleDto> getArticle(@PathVariable("id") Long id) {
		log.info("Getting article by id = {}", id);
		return articleService.getArticle(id)
				.map(article -> {
					log.info("Found article = {}", article);
					return new ResponseEntity<>(article, OK);
				})
				.orElseGet(() -> {
					log.warn(ARTICLE_NOT_FOUND_MESSAGE, id);
					return new ResponseEntity<>(NOT_FOUND);
				});
	}

	@PostMapping(
			produces = APPLICATION_JSON_VALUE,
			consumes = APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ArticleDto> createArticle(@RequestBody CreateArticleRequest request) {
		log.info("Creating article = {}", request);
		ArticleDto article = articleService.createArticle(request);
		log.info("Article created = {}", article);
		return new ResponseEntity<>(article, CREATED);
	}

	@PutMapping(
			value = "{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ArticleDto> updateArticle(@PathVariable("id") Long id,
			@RequestBody UpdateArticleRequest request) {
		log.info("Updating article with id = {} by request = {}", id, request);
		return articleService.updateArticle(id, request)
				.map(article -> {
					log.info("Updated article = {}", article);
					return new ResponseEntity<>(article, OK);
				})
				.orElseGet(() -> {
					log.warn(ARTICLE_NOT_FOUND_MESSAGE, id);
					return new ResponseEntity<>(NOT_FOUND);
				});
	}

	@DeleteMapping(
			value = "{id}"
	)
	public void deleteArticle(@PathVariable("id") Long id) {
		log.info("Deleting article with id = {}", id);
		articleService.deleteArticle(id);
	}
}
