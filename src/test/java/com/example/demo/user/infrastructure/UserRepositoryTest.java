package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저데이터를_찾아올_수_있다() {
        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        Optional<UserEntity> byIdAndStatus = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);
        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저데이터를_찾아올_수_있다() {
        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("bht9011@gmail.com", UserStatus.ACTIVE);

        assertThat(byIdAndStatus.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        Optional<UserEntity> byIdAndStatus = userRepository.findByEmailAndStatus("bht9011@gmail.com", UserStatus.PENDING);
        assertThat(byIdAndStatus.isPresent()).isFalse();
        assertThat(byIdAndStatus.isEmpty()).isTrue();
    }

}