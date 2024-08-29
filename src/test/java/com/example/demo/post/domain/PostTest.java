package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void PostCreate_로_게시물을_만들_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(2L)
                .content("내용")
                .build();
        User writer = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();
        //when
        Post post = Post.from(writer, postCreate, new TestClockHolder(12381728L));

        //then
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getWriter().getId()).isEqualTo(2L);
        assertThat(post.getWriter().getNickname()).isEqualTo("coen");
        assertThat(post.getWriter().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }

    @Test
    void PostUpdate_로_게시물을_수정_할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(2L)
                .content("내용")
                .build();
        User writer = User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();
        Post postOriginal = Post.from(writer, postCreate, new TestClockHolder(12381728L));
        PostUpdate postUpdate = PostUpdate.builder()
                .content("수정된 내용")
                .build();
        //when
        Post post = postOriginal.update(postUpdate, new TestClockHolder(12381728L));

        //then
        assertThat(post.getContent()).isEqualTo("수정된 내용");
        assertThat(post.getWriter().getId()).isEqualTo(2L);
        assertThat(post.getWriter().getNickname()).isEqualTo("coen");
        assertThat(post.getWriter().getEmail()).isEqualTo("bht9011@gmail.com");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}