package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Sql("/sql/user-service-test-data.sql")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByEmail_은_ACTIVE_유저데이터를_찾아올_수_있다() {
        //given
        String email = "bht9011@gmail.com";

        //when
        UserEntity result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("coen");
    }

    @Test
    void getByEmail_은_PENDING_유저데이터를_찾아올_수_없다() {
        //given
        String email = "bht9012@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById_은_ACTIVE_유저데이터를_찾아올_수_있다() {
        //given
        String email = "bht9012@gmail.com";

        //when
        UserEntity result = userService.getById(1L);

        //then
        assertThat(result.getNickname()).isEqualTo("coen");
    }

    @Test
    void getById_은_PENDING_유저데이터를_찾아올_수_없다() {
        //given
        String email = "bht9012@gmail.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getById(2L))
            .isInstanceOf(ResourceNotFoundException.class);
    }



}