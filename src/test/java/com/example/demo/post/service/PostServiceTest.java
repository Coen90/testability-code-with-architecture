package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postService = PostService.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(12381728L))
                .build();

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("bht9011@gmail.com")
                .nickname("coen")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        fakeUserRepository.save(User.builder()
                .id(3L)
                .email("bht9012@gmail.com")
                .nickname("cooo")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
        fakePostRepository.save(Post.builder()
                .id(2L)
                .writer(fakeUserRepository.getById(2l))
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .build());
    }

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
        assertThat(result.getCreatedAt()).isEqualTo(12381728L);
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
        assertThat(result.getModifiedAt()).isEqualTo(12381728L);
    }

}