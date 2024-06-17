package com.example.blog.controller;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.example.blog.controller.dto.CreateArticleRequest;
import com.example.blog.controller.dto.UpdateArticleRequest;
import com.example.blog.model.Article;

class ArticleControllerTest extends AbstractControllerTest {

	@Test
	public void testGetArticles() throws Exception {
		Article article = createArticle();

		getMockMvc().perform(get("/blog/articles")
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(article.getId().intValue())))
				.andExpect(jsonPath("$[0].name", is(article.getName())))
				.andExpect(jsonPath("$[0].published",
						is(article.getPublished().format(ofPattern("yyyy-MM-dd")))));
	}

	@Test
	public void testGetArticlesBeforeDate() throws Exception {
		createArticles();

		getMockMvc().perform(get("/blog/articles/before-date")
						.param("date", ARTICLE_PUBLISHED.plusDays(3).format(ofPattern("yyyy-MM-dd")))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is(ARTICLE_NAME)))
				.andExpect(jsonPath("$[0].published",
						is(ARTICLE_PUBLISHED.format(ofPattern("yyyy-MM-dd")))))

				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("name2")))
				.andExpect(jsonPath("$[1].published",
						is(ARTICLE_PUBLISHED.plusDays(1).format(ofPattern("yyyy-MM-dd")))))

				.andExpect(jsonPath("$.*", hasSize(2)));
	}

	@Test
	public void testGetArticlesBeforeDateBadRequest() throws Exception {
		getMockMvc().perform(get("/blog/articles/before-date")
						.param("date", "20245-12-01")
						.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetArticle() throws Exception {
		Article article = createArticle();

		getMockMvc().perform(get("/blog/articles/" + article.getId())
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("id", is(article.getId().intValue())))
				.andExpect(jsonPath("name", is(article.getName())))
				.andExpect(jsonPath("published",
						is(article.getPublished().format(ofPattern("yyyy-MM-dd")))));
	}

	@Test
	public void testGetArticleIdNotExist() throws Exception {
		getMockMvc().perform(get("/blog/articles/1")
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateArticle() throws Exception {
		CreateArticleRequest request = new CreateArticleRequest();
		request.setName(ARTICLE_NAME);
		request.setPublished(ARTICLE_PUBLISHED);

		getMockMvc().perform(post("/blog/articles")
						.contentType(APPLICATION_JSON)
						.content(getObjectMapper().writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is(ARTICLE_NAME)))
				.andExpect(jsonPath("published", is(ARTICLE_PUBLISHED.format(ofPattern("yyyy-MM-dd")))));
	}

	@Test
	public void testUpdateArticle() throws Exception {
		Article article = createArticle();

		UpdateArticleRequest request = new UpdateArticleRequest();
		request.setName("name2");
		request.setPublished(ARTICLE_PUBLISHED.plusDays(10));

		getMockMvc().perform(put("/blog/articles/" + article.getId())
						.contentType(APPLICATION_JSON)
						.content(getObjectMapper().writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("name2")))
				.andExpect(jsonPath("published", is(ARTICLE_PUBLISHED.plusDays(10).format(ofPattern("yyyy-MM-dd")))));
	}

	@Test
	public void testUpdateArticleIdNotExist() throws Exception {
		getMockMvc().perform(put("/blog/articles/1")
						.contentType(APPLICATION_JSON)
						.content(getObjectMapper().writeValueAsString(new UpdateArticleRequest())))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteArticle() throws Exception {
		Article article = createArticle();

		getMockMvc().perform(delete("/blog/articles/" + article.getId())
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk());

		getMockMvc().perform(get("/blog/articles/" + article.getId())
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}