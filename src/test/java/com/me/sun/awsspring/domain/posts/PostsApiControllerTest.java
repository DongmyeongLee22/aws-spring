package com.me.sun.awsspring.domain.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.sun.awsspring.web.dto.PostsSaveRequestDto;
import com.me.sun.awsspring.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class PostsApiControllerTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Post_등록() throws Exception {
        //given
        String title = "제목";
        String content = "본문";
        String author = "123123@gmail.com";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "/api/v1/posts";

        //when
        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))
        )
                .andDo(print())
                .andExpect(status().isOk());


        //then
        List<Posts> postsList = postsRepository.findAll();
        Posts getPosts = postsList.get(0);

        assertThat(getPosts.getTitle()).isEqualTo(title);
        assertThat(getPosts.getContent()).isEqualTo(content);
        assertThat(getPosts.getAuthor()).isEqualTo(author);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Post_수정() throws Exception {
        //given
        String title = "제목";
        String content = "본문";
        String author = "123123@gmail.com";
        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        Posts savedPosts = postsRepository.save(posts);

        Long updateId = savedPosts.getId();

        String expectedTitle = "변경된 제목";
        String expectedContent = "변경된 본문";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "/api/v1/posts/" + updateId;


        //when
        this.mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))
        )
                .andExpect(status().isOk());


        //then

        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(postsList.get(0).getContent()).isEqualTo(expectedContent);
    }


}