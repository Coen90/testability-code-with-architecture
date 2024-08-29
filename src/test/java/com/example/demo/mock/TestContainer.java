package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.common.service.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.service.CertificationServiceImpl;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserReadService userReadService;
    public final UserUpdateService userUpdateService;
    public final UserCreateService userCreateService;
    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .build();
        this.certificationService = new CertificationServiceImpl(mailSender);
        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .certificationService(new CertificationServiceImpl(mailSender))
                .clockHolder(clockHolder)
                .userRepository(userRepository)
                .build();

        this.userReadService = userService;
        this.userUpdateService = userService;
        this.userCreateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                .authenticationService(this.authenticationService)
                .userReadService(this.userReadService)
                .userCreateService(this.userCreateService)
                .userUpdateService(this.userUpdateService)
                .build();
        this.userCreateController = new UserCreateController(this.userCreateService);
        this.postController = new PostController(this.postService);
        this.postCreateController = new PostCreateController(this.postService);
    }

}
