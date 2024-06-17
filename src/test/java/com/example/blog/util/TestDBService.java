package com.example.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.repository.ArticleRepository;

public class TestDBService {

	@Autowired
	private ArticleRepository articleRepository;

	@Transactional
	public void cleanUpDatabase() {
		articleRepository.deleteAll();
	}
}
