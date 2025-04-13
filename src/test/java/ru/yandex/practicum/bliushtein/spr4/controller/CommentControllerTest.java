package ru.yandex.practicum.bliushtein.spr4.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.bliushtein.spr4.controller.configuration.ControllerTestConfiguration;
import ru.yandex.practicum.bliushtein.spr4.service.CommentService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class CommentControllerTest {

    @Mock
    CommentService commentService;
    @InjectMocks
    CommentController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }



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
