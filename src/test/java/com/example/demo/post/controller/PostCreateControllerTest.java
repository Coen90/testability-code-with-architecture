package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateControllerTest {

    @Test
    void Post_를_생성한다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 12381924L)
                .build();
        PostCreateController postCreateController = testContainer.postCreateController;
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("서울시 광진구")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        // when
        ResponseEntity<PostResponse> result = postCreateController.create(PostCreate.builder()
                .content("컨텐츠")
                .writerId(2L)
                .build());
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("컨텐츠");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(12381924L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("coen");
    }

}