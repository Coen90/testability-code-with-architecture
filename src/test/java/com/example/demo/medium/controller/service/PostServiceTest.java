package com.example.demo.medium.controller.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getPostById_은_존재하는_id로_호출하면_해당_게시글을_반환한다() {
        // given
        long id = 2L;

        // when
        var result = postService.getPostById(id);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void getPostById_은_존재하지_않는_id로_호출하면_에러를_반환한다() {
        // given
        long id = 1L;
        // when
        // then
        assertThatThrownBy(() -> postService.getPostById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_는_게시물을_생성한다() {
        // given
        var postCreateDto = PostCreate.builder()
                .writerId(2L)
                .content("contenttttt")
                .build();

        // when
        var result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("contenttttt");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
        assertThat(result.getWriter().getNickname()).isEqualTo("coen");
    }

    @Test
    void update_는_게시물을_수정한다() {
        // given
        long id = 2L;
        var postUpdateDto = PostUpdate.builder()
                .content("updated content")
                .build();

        // when
        var result = postService.update(id, postUpdateDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("updated content");
        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }

}