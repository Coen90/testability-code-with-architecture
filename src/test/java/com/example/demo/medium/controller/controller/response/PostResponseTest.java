package com.example.demo.medium.controller.controller.response;

import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostResponseTest {

    @Test
    void Post_로_PostResponse_응답을_만들_수_있다() {
        //given
        Post post = Post.builder()
                .content("내용")
                .writer(User.builder()
                        .id(2L)
                        .email("bht9011@gmail.com")
                        .nickname("coen")
                        .address("seoul")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .build())
                .build();
        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo("내용");
        assertThat(postResponse.getWriter().getId()).isEqualTo(2L);
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("coen");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}