package ru.yandex.practicum.bliushtein.spr4.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.bliushtein.spr4.data.repository.PostRepository;
import ru.yandex.practicum.bliushtein.spr4.service.PostService;
import ru.yandex.practicum.bliushtein.spr4.service.UpdateImageService;
import ru.yandex.practicum.bliushtein.spr4.service.dto.CommentInfo;
import ru.yandex.practicum.bliushtein.spr4.service.dto.ImageOperation;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostSummary;

import java.util.List;

import static org.mockito.Mockito.*;
import static ru.yandex.practicum.bliushtein.spr4.service.impl.configuration.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PostServiceImpl.class})
public class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostRepository postRepositoryMock;

    @MockitoBean
    private UpdateImageService imageServiceMock;

    @Mock
    ImageOperation imageOperationMock;

    @BeforeEach
    void setUp() {
        reset(imageOperationMock);
    }

    @Test
    void testFindAll() {
        when(postRepositoryMock.getAllPosts()).thenReturn(POST_LIST);
        when(postRepositoryMock.getTags(POST.getId())).thenReturn(TAGS_1);
        when(postRepositoryMock.getCommentsCount(POST.getId())).thenReturn(COMMENTS_COUNT);
        List<PostSummary> postSummaries = postService.findAll();
        validatePostSummaries(postSummaries);
    }

    @Test
    void testFindByTag() {
        when(postRepositoryMock.getPostsByTag(TAG_1)).thenReturn(POST_LIST);
        when(postRepositoryMock.getTags(POST.getId())).thenReturn(TAGS_1);
        when(postRepositoryMock.getCommentsCount(POST.getId())).thenReturn(COMMENTS_COUNT);
        List<PostSummary> postSummaries = postService.findByTag(TAG_1);
        validatePostSummaries(postSummaries);

    }

    @Test
    void testGetPostDetails() {
        when(postRepositoryMock.getPost(POST.getId())).thenReturn(POST);
        when(postRepositoryMock.getTags(POST.getId())).thenReturn(TAGS_1);
        when(postRepositoryMock.getCommentsCount(POST.getId())).thenReturn(COMMENTS_COUNT);
        when(postRepositoryMock.getComments(POST.getId())).thenReturn(COMMENTS);
        validatePostDetails(postService.getPostDetails(POST.getId()));
    }

    @Test
    void testAddLike() {
        postService.addLike(POST.getId());
        verify(postRepositoryMock).addLike(POST.getId());
    }

    @Test
    void testRemoveLike() {
        postService.removeLike(POST.getId());
        verify(postRepositoryMock).removeLike(POST.getId());
    }

    @Test
    void testDeletePost() {
        postService.deletePost(POST.getId(), imageOperationMock);
        verify(postRepositoryMock).deletePost(POST.getId());
        verify(imageServiceMock).processImage(imageOperationMock);
    }

    @Test
    void testCreatePost() {
        when(imageServiceMock.processImage(imageOperationMock)).thenReturn(POST.getImageId());
        postService.createPost(POST.getName(), POST.getFullText(), TAGS_1, imageOperationMock);
        verify(postRepositoryMock).createPost(POST.getName(), POST.getFullText(), POST.getShortText(), POST.getImageId());
        verify(imageServiceMock).processImage(imageOperationMock);
        verify(postRepositoryMock).createTag(any(), eq(TAG_1));
    }

    @Test
    void testUpdatePost() {
        when(imageServiceMock.processImage(imageOperationMock)).thenReturn(POST.getImageId());
        when(postRepositoryMock.getTags(POST.getId())).thenReturn(TAGS_2);
        postService.updatePost(POST.getId(), POST.getName(), POST.getFullText(), TAGS_1, imageOperationMock);
        verify(postRepositoryMock).updatePost(POST.getId(), POST.getName(), POST.getFullText(), POST.getShortText(), POST.getImageId());
        verify(imageServiceMock).processImage(imageOperationMock);
        verify(postRepositoryMock).createTag(POST.getId(), TAG_1);
        verify(postRepositoryMock).deleteTags(POST.getId(), TAGS_2);
    }

    private static void validatePostSummaries(List<PostSummary> postSummaries) {
        assertEquals(POST_LIST.size(), postSummaries.size());
        validatePostSummary(postSummaries.getFirst());
    }

    private static void validatePostSummary(PostSummary postSummary) {
        assertEquals(TAGS_1, postSummary.getTags());
        assertEquals(COMMENTS_COUNT, postSummary.getCommentsCount());
        assertEquals(POST.getName(), postSummary.getName());
        assertEquals(POST.getShortText(), postSummary.getShortText());
        assertEquals(POST.getImageId(), postSummary.getImageKey());
        assertEquals(POST.getCreatedWhen(), postSummary.getCreatedWhen());
        assertEquals(POST.getLikesCount(), postSummary.getLikesCount());
    }

    private static void validatePostDetails(PostDetails postDetails) {
        validatePostSummary(postDetails);
        assertEquals(COMMENTS.size(), postDetails.getComments().size());
        validateComment(postDetails.getComments().getFirst());
        assertEquals(POST.getFullText(), postDetails.getFullText());
    }

    private static void validateComment(CommentInfo commentInfo) {
        assertEquals(COMMENT.getId(), commentInfo.getId());
        assertEquals(COMMENT.getCreatedWhen(), commentInfo.getCreatedWhen());
        assertEquals(COMMENT.getText(), commentInfo.getText());
    }
}
