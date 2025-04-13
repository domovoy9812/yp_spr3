package ru.yandex.practicum.bliushtein.spr4.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.bliushtein.spr4.service.CommentService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;
import static org.mockito.Mockito.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @MockitoBean
    CommentService commentService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddComment() throws Exception {
        mockMvc.perform(post("/post/{postId}/comment/add",
                        POST_ID.toString())
                        .param("text", COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + POST_ID));
        verify(commentService).addComment(POST_ID, COMMENT_TEXT);
    }

    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(post("/post/{postId}/comment/{id}/delete",
                        POST_ID.toString(),
                        COMMENT_ID.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + POST_ID));
        verify(commentService).deleteComment(COMMENT_ID);
    }

    @Test
    void testUpdateComment() throws Exception {
        mockMvc.perform(post("/post/{postId}/comment/{id}/update",
                        POST_ID.toString(),
                        COMMENT_ID.toString())
                        .param("text", COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + POST_ID));
        verify(commentService).updateComment(COMMENT_ID, COMMENT_TEXT);
    }
}
