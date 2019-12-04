package com.me.sun.awsspring.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() throws Exception {
        //given
        String title = "제목";
        String content = "본문";
        String author = "123123@gmail.com";

        Posts post = Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        postsRepository.save(post);

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts getPosts = postsList.get(0);
        assertThat(getPosts.getTitle()).isEqualTo(title);
        assertThat(getPosts.getContent()).isEqualTo(content);
        assertThat(getPosts.getAuthor()).isEqualTo(author);
    }

    @Test
    public void BaseTimeEntity_테스트() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        Posts posts = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();
        postsRepository.save(posts);

        //when
        List<Posts> all = postsRepository.findAll();

        //then
        Posts findPosts = all.get(0);

        System.out.println("==============================");
        System.out.println(findPosts.getCreatedDate());
        System.out.println(findPosts.getModifiedDate());
        System.out.println("==============================");

        assertThat(findPosts.getCreatedDate()).isAfter(now);
        assertThat(findPosts.getModifiedDate()).isAfter(now);
    }


}