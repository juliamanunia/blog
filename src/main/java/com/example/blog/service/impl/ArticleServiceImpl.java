package com.example.blog.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.controller.dto.ArticleDto;
import com.example.blog.controller.dto.CreateArticleRequest;
import com.example.blog.controller.dto.UpdateArticleRequest;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.model.Article;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.service.ArticleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

	private final ArticleRepository articleRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ArticleDto> getArticles() {
		return articleRepository.findAll()
				.stream()
				.map(ArticleMapper.ARTICLE::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ArticleDto> getArticle(Long id) {
		return articleRepository.findById(id)
				.map(ArticleMapper.ARTICLE::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ArticleDto> getArticlesBeforeDate(LocalDate beforeDate) {
		return articleRepository.findAllBeforeDate(beforeDate)
				.stream()
				.map(ArticleMapper.ARTICLE::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ArticleDto createArticle(CreateArticleRequest request) {
		Article article = new Article();
		article.setName(request.getName());
		article.setPublished(request.getPublished());

		Article savedArticle = articleRepository.save(article);

		return ArticleMapper.ARTICLE.toDto(savedArticle);
	}

	@Override
	@Transactional
	public Optional<ArticleDto> updateArticle(Long id, UpdateArticleRequest request) {
		return articleRepository.findById(id)
				.map(article -> doUpdateArticle(request, article))
				.map(ArticleMapper.ARTICLE::toDto);
	}

	@Override
	@Transactional
	public void deleteArticle(Long id) {
		articleRepository.findById(id).ifPresent(article -> {
			articleRepository.deleteById(id);
			log.info("Article with id = {} was deleted", id);
		});
	}

	private Article doUpdateArticle(UpdateArticleRequest request, Article article) {
		Optional.ofNullable(request.getName())
				.ifPresent(article::setName);

		Optional.ofNullable(request.getPublished())
				.ifPresent(article::setPublished);

		return article;
	}
}
