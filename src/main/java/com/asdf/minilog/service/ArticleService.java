package com.asdf.minilog.service;

import com.asdf.minilog.dto.ArticleResponseDto;
import com.asdf.minilog.entity.Article;
import com.asdf.minilog.entity.User;
import com.asdf.minilog.exception.UserNotFoundException;
import com.asdf.minilog.repository.ArticleRepository;
import com.asdf.minilog.repository.UserRepository;
import com.asdf.minilog.util.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ArticleResponseDto createArticle(String content, Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new UserNotFoundException(
                                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId)));
        Article article = Article.builder().content(content).author(user).build();
        Article savedArticle = articleRepository.save(article);
        return EntityDtoMapper.toDto(savedArticle);
    }
}
