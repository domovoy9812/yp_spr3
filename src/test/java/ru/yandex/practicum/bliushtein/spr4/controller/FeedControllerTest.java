package ru.yandex.practicum.bliushtein.spr4.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.bliushtein.spr4.controller.configuration.ControllerTestConfiguration;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostSummary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static ru.yandex.practicum.bliushtein.spr4.controller.configuration.TestData.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class FeedControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostService postServiceMock;

    @Autowired
    private FeedController controller;
    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        reset(postServiceMock);
    }

    @Test
    void test_initialLoadOfAllPosts() throws Exception {
        when(postServiceMock.findAll()).thenReturn(POST_SUMMARY_LIST_SIZE_6);
        callFeedPage(StringUtils.EMPTY, POST_SUMMARY_LIST_SIZE_6);
        assertPostsStoredInSession(POST_SUMMARY_LIST_SIZE_6);
        assertEquals(StringUtils.EMPTY, session.getAttribute("tag"));
    }

    @Test
    void test_initialLoadOfFoundPostsByTag() throws Exception {
        when(postServiceMock.findByTag(SEARCH_TAG)).thenReturn(POST_SUMMARY_LIST_SIZE_6);
        callFeedPage(SEARCH_TAG, POST_SUMMARY_LIST_SIZE_6);
        assertPostsStoredInSession(POST_SUMMARY_LIST_SIZE_6);
        assertEquals(SEARCH_TAG, session.getAttribute("tag"));
    }

    @Test
    void test_reloadOfFoundPostsByTag() throws Exception {
        session.setAttribute("posts", new PagedListHolder<>(POST_SUMMARY_LIST_SIZE_1));
        session.setAttribute("tag", SEARCH_TAG_2);
        when(postServiceMock.findByTag(SEARCH_TAG)).thenReturn(POST_SUMMARY_LIST_SIZE_6);
        callFeedPage(SEARCH_TAG, POST_SUMMARY_LIST_SIZE_6);
        assertPostsStoredInSession(POST_SUMMARY_LIST_SIZE_6);
        assertEquals(SEARCH_TAG, session.getAttribute("tag"));
    }

    @Test
    void test_getFromSessionCache() throws Exception {
        session.setAttribute("posts", new PagedListHolder<>(POST_SUMMARY_LIST_SIZE_6));
        session.setAttribute("tag", SEARCH_TAG);
        callFeedPage(SEARCH_TAG, POST_SUMMARY_LIST_SIZE_6);
        verify(postServiceMock, never()).findAll();
        verify(postServiceMock, never()).findByTag(SEARCH_TAG);
    }

    private void assertPostsStoredInSession(List<PostSummary> posts) {
        PagedListHolder<PostSummary> pagedPosts = (PagedListHolder<PostSummary>) session.getAttribute("posts");
        assertEquals(posts, pagedPosts.getSource());
    }

    private void callFeedPage(String searchTag, List<PostSummary> posts) throws Exception {
        mockMvc.perform(get("/feed")
                        .param("tag", searchTag)
                        .param("pageSize", "5")
                        .param("pageNumber", "0")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                //Last element on page 2 and shouldn't be visible
                .andExpect(xpath("//tr/td/p[text() = \"%s\"]", posts.getLast().getShortText())
                        .doesNotExist())
                //First element on page 1 and should be visible
                .andExpect(xpath("//tr/td/p[text() = \"%s\"]", posts.getFirst().getShortText())
                        .exists());
    }
}
