package ru.yandex.practicum.bliushtein.spr4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostDetails;

import static org.mockito.Mockito.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @MockitoBean
    private PostService postServiceMock;

    @Autowired
    private MockMvc mockMvc;
    @Test
    void testShowPost() throws Exception {
        when(postServiceMock.getPostDetails(POST_ID)).thenReturn(POST_DETAILS);
        mockMvc.perform(get("/post/{id}", POST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", POST_DETAILS))
                .andExpect(xpath("//tr/td/h2[text() = \"%s\"]", POST_DETAILS.getName()).exists())
                .andExpect(xpath("//tr/td/p/span[text() = \"#%s\"]",
                        POST_DETAILS.getTags().getFirst()).exists())
                .andExpect(xpath("//tr/td/form/span[text() = \"%s\"]",
                        POST_DETAILS.getComments().getFirst().getText()).exists());
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(post("/post/{id}/delete", POST_ID.toString())
                        .param("image_key", IMAGE_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/feed"));
        verify(postServiceMock).deletePost(eq(POST_ID), argThat(
                io -> io.getKey().equals(IMAGE_ID) && io.getAction() == ImageOperation.Action.DELETE));
    }

    @Test
    void testEditPost() throws Exception {
        when(postServiceMock.getPostDetails(POST_ID)).thenReturn(POST_DETAILS);
        mockMvc.perform(get("/post/{id}/edit", POST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post", POST_DETAILS))
                .andExpect(xpath("//tr/td/textarea[text() = \"%s\"]", POST_DETAILS.getName()).exists())
                .andExpect(xpath("//tr/td/input[@value = \"%s\"]",
                        POST_DETAILS.getTags().getFirst()).exists())
                .andExpect(xpath("//tr/td/textarea[text() = \"%s\"]",
                        POST_DETAILS.getFullText()).exists());
    }

    @Test
    void testNewPost() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post", PostDetails.EMPTY));
    }

    @Test
    void testUpdatePost() throws Exception {
        when(postServiceMock.getPostDetails(POST_DETAILS.getId())).thenReturn(POST_DETAILS);
        mockMvc.perform(post("/post/{id}/edit", POST_DETAILS.getId().toString())
                        .param("tag", POST_DETAILS.getTags().toArray(ENPTY_STRING_ARRAY))
                        .param("text", POST_DETAILS.getFullText())
                        .param("name", POST_DETAILS.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + POST_DETAILS.getId()));
        verify(postServiceMock).updatePost(POST_DETAILS.getId(), POST_DETAILS.getName(), POST_DETAILS.getFullText(),
                POST_DETAILS.getTags(), SpringImageOperation.SAME_IMAGE_INFO);
    }

    @Test
    void testCreatePost() throws Exception {
        when(postServiceMock.createPost(POST_DETAILS.getName(), POST_DETAILS.getFullText(), POST_DETAILS.getTags(),
                        SpringImageOperation.SAME_IMAGE_INFO)).thenReturn(POST_DETAILS.getId());
        mockMvc.perform(post("/post/create")
                        .param("tag", POST_DETAILS.getTags().toArray(ENPTY_STRING_ARRAY))
                        .param("text", POST_DETAILS.getFullText())
                        .param("name", POST_DETAILS.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + POST_DETAILS.getId()));
    }

    @Test
    void testAddLike() throws Exception {
        when(postServiceMock.getPostDetails(POST_DETAILS.getId())).thenReturn(POST_DETAILS);
        mockMvc.perform(post("/post/{id}/like", POST_DETAILS.getId().toString())
                        .param("like", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", POST_DETAILS))
                .andExpect(view().name("post"));
        verify(postServiceMock).addLike(POST_DETAILS.getId());
    }

    @Test
    void testRemoveLike() throws Exception {
        when(postServiceMock.getPostDetails(POST_DETAILS.getId())).thenReturn(POST_DETAILS);
        mockMvc.perform(post("/post/{id}/like", POST_DETAILS.getId().toString())
                        .param("like", "false"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", POST_DETAILS))
                .andExpect(view().name("post"));
        verify(postServiceMock).removeLike(POST_DETAILS.getId());
    }
}
