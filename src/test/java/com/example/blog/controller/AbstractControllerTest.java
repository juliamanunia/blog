package com.example.blog.controller;

import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.blog.BlogApplication;
import com.example.blog.model.Article;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.util.TestContext;
import com.example.blog.util.TestDBService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = {BlogApplication.class, TestContext.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class AbstractControllerTest {

	@Autowired
	private TestDBService testDBService;

	@Getter(PROTECTED)
	@Autowired
	private ObjectMapper objectMapper;

	@Getter(PROTECTED)
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ArticleRepository articleRepository;

	protected static final LocalDate ARTICLE_PUBLISHED = LocalDate.parse("2024-06-04", ofPattern("yyyy-MM-dd"));
	protected static final String ARTICLE_NAME = "name1";

	@BeforeEach
	public void setUp() {
		testDBService.cleanUpDatabase();
	}

	protected Article createArticle() {
		Article article = new Article();
		article.setName(ARTICLE_NAME);
		article.setPublished(ARTICLE_PUBLISHED);
		return articleRepository.save(article);
	}

	protected void createArticles() {
		createArticle();

		Article article = new Article();
		article.setName("name2");
		article.setPublished(ARTICLE_PUBLISHED.plusDays(1));
		articleRepository.save(article);

		Article article2 = new Article();
		article.setName("name3");
		article.setPublished(ARTICLE_PUBLISHED.plusDays(3));
		articleRepository.save(article2);
	}
}
