package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    //소형테스트는 싱글스레드로 돌아가기 때문에 동기화 처리를 하지 않아도 된다. 그냥 Long, Array로 해도 됨
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<User> data = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream()
                .filter(item -> Objects.equals(item.getId(), id) && item.getStatus() == userStatus)
                .findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream()
                .filter(item -> Objects.equals(item.getEmail(), email) && item.getStatus() == userStatus)
                .findAny();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .certificationCode(user.getCertificationCode())
                    .status(user.getStatus())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return data.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findAny();
    }

    @Override
    public User getById(long writerId) {
        return findById(writerId).orElseThrow(() -> new ResourceNotFoundException("Users", writerId));
    }
}
