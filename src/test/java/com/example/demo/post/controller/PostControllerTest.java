package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostControllerTest {

    @Test
    void Post_를_조회한다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        PostController postController = testContainer.postController;
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("컨텐츠")
                .createdAt(1000L)
                .writer(User.builder()
                        .id(2L)
                        .email("bht9011@gmail.com")
                        .nickname("coen")
                        .address("서울시 광진구")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L)
                        .build())
                .build());
        //when
        ResponseEntity<PostResponse> result = postController.getPostById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("컨텐츠");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1000L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("coen");
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void Post_를_없는_id로_조회하면_ResourceNotFoundException이_발생한다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        PostController postController = testContainer.postController;
        //when
        //then
        assertThatThrownBy(() -> postController.getPostById(1298L))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void Post_를_수정한다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 12358398L)
                .build();
        PostController postController = testContainer.postController;
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("컨텐츠")
                .createdAt(1000L)
                .writer(User.builder()
                        .id(2L)
                        .email("bht9011@gmail.com")
                        .nickname("coen")
                        .address("서울시 광진구")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L)
                        .build())
                .build());
        //when
        ResponseEntity<PostResponse> result = postController.update(1L, PostUpdate.builder().content("modified").build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("modified");
        assertThat(result.getBody().getModifiedAt()).isEqualTo(12358398L);
    }
}