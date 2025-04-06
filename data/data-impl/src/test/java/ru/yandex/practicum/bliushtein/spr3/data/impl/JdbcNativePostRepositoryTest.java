package ru.yandex.practicum.bliushtein.spr3.data.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.bliushtein.spr3.data.repository.DataAccessException;
import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;
import ru.yandex.practicum.bliushtein.spr3.data.repository.PostRepository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
@Tag("Integration")
public class JdbcNativePostRepositoryTest {

    private static final int INITIAL_POSTS_COUNT = 3;

    private static final UUID FIRST_POST_ID = UUID.fromString("ba6136ae-58c6-488a-8c18-77660307cf3c");
    private static final int FIRST_POST_LIKES_COUNT = 10;
    private static final String FIRST_POST_NAME = "first post name";
    private static final String FIRST_POST_FULL_TEXT = "first post full text";
    private static final String FIRST_POST_SHORT_TEXT = "first post short text";
    private static final UUID FIRST_POST_IMAGE_ID = UUID.fromString("eaa94221-036b-4cc3-a777-7c4ee4f10d67");
    private static final int FIRST_POST_TAG_COUNT = 1;

    private static final UUID SECOND_POST_ID = UUID.fromString("f9440871-fa16-47f6-987f-165b994f1b27");
    private static final int SECOND_POST_COMMENTS_COUNT = 2;

    private static final UUID THIRD_POST_ID = UUID.fromString("e6b1b47c-6e33-45c3-a3f7-5e5768ab55ce");
    private static final UUID INCORRECT_ID = new UUID(0, 0);

    private static final String FIRST_TAG_NAME = "first tag";
    private static final String SECOND_TAG_NAME = "second tag";
    private static final String NOT_EXISTING_TAG_NAME = "not existing tag";

    private static final UUID FIRST_COMMENT_ID = UUID.fromString("65558158-7484-4868-90f4-15e72189b02d");
    private static final String SECOND_COMMENT = "second comment";
    private static final String THIRD_COMMENT = "third comment";
    private static final String NEW_COMMENT = "new comment";

    private static final int ZERO_LIKES_COUNT = 0;
    private static final String NEW_POST_NAME = "new post";
    private static final String NEW_POST_FULL_TEXT = "full text";
    private static final String NEW_POST_SHORT_TEXT = "short text";
    private static final UUID NEW_IMAGE_ID = UUID.fromString("2fc4ea9d-a12d-4a6f-9aaf-79585e3f9a71");

    @Value("classpath:db/init_test_data.sql")
    private Resource initTestDataScript;

    @Autowired
    PostRepository repository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(initTestDataScript);
        populator.execute(dataSource);
    }

    @Test
    void testGetAllPosts() {
        List<Post> posts = repository.getAllPosts();
        assertEquals(INITIAL_POSTS_COUNT, posts.size());
        assertTrue(posts.stream().map(Post::getId).toList()
                .containsAll(List.of(FIRST_POST_ID, SECOND_POST_ID, THIRD_POST_ID)));
    }

    @Test
    void testGetTags() {
        List<String> tags = repository.getTags(FIRST_POST_ID);
        assertEquals(FIRST_POST_TAG_COUNT, tags.size());
        assertTrue(tags.contains(FIRST_TAG_NAME));
        tags = repository.getTags(SECOND_POST_ID);
        assertTrue(tags.isEmpty());
    }

    @Test
    void testGetTags_negative() {
        assertThrows(DataAccessException.class, () -> repository.getTags(null));
        assertThrows(DataAccessException.class, () -> repository.getTags(INCORRECT_ID));
    }

    @Test
    void testGetCommentsCount() {
        assertEquals(SECOND_POST_COMMENTS_COUNT, repository.getCommentsCount(SECOND_POST_ID));
    }

    @Test
    void testGetCommentsCount_negative() {
        assertThrows(DataAccessException.class, () -> repository.getCommentsCount(null));
        assertThrows(DataAccessException.class, () -> repository.getCommentsCount(INCORRECT_ID));
    }

    @Test
    void testGetPostsByTag() {
        List<UUID> ids = repository.getPostsByTag(FIRST_TAG_NAME).stream().map(Post::getId).toList();
        assertTrue(ids.containsAll(List.of(FIRST_POST_ID, THIRD_POST_ID)));
        assertFalse(ids.contains(SECOND_POST_ID));

        ids = repository.getPostsByTag(SECOND_TAG_NAME).stream().map(Post::getId).toList();
        assertTrue(ids.contains(THIRD_POST_ID));
        assertFalse(ids.contains(FIRST_POST_ID));
        assertFalse(ids.contains(SECOND_POST_ID));

        assertTrue(repository.getPostsByTag(NOT_EXISTING_TAG_NAME).isEmpty());
    }

    @Test
    void testGetPostsByTag_negative() {
        assertThrows(DataAccessException.class, () -> repository.getPostsByTag(null));
    }

    @Test
    void testGetComments() {
        List<String> comments = repository.getComments(SECOND_POST_ID).stream().map(Comment::getText).toList();
        assertEquals(SECOND_POST_COMMENTS_COUNT, comments.size());
        assertTrue(comments.containsAll(List.of(SECOND_COMMENT, THIRD_COMMENT)));
    }

    @Test
    void testGetComments_negative() {
        assertThrows(DataAccessException.class, () -> repository.getComments(null));
        assertThrows(DataAccessException.class, () -> repository.getComments(INCORRECT_ID));
    }

    @Test
    void testGetPost() {
        Post post = repository.getPost(FIRST_POST_ID);
        assertEquals(FIRST_POST_ID, post.getId());
        assertEquals(FIRST_POST_NAME, post.getName());
        assertEquals(FIRST_POST_FULL_TEXT, post.getFullText());
        assertEquals(FIRST_POST_SHORT_TEXT, post.getShortText());
        assertEquals(FIRST_POST_LIKES_COUNT, post.getLikesCount());
        assertEquals(FIRST_POST_IMAGE_ID, post.getImageId());
    }

    @Test
    void testGetPost_negative() {
        assertThrows(DataAccessException.class, () -> repository.getPost(null));
        assertThrows(DataAccessException.class, () -> repository.getPost(INCORRECT_ID));
    }

    @Test
    void testAddLike() {
        repository.addLike(FIRST_POST_ID);
        assertEquals(FIRST_POST_LIKES_COUNT + 1, repository.getPost(FIRST_POST_ID).getLikesCount());
    }
    @Test
    void testAddLike_negative() {
        assertThrows(DataAccessException.class, () -> repository.addLike(null));
        assertThrows(DataAccessException.class, () -> repository.addLike(INCORRECT_ID));
    }
    @Test
    void testRemoveLike() {
        repository.removeLike(FIRST_POST_ID);
        assertEquals(FIRST_POST_LIKES_COUNT - 1, repository.getPost(FIRST_POST_ID).getLikesCount());

        assertEquals(ZERO_LIKES_COUNT, repository.getPost(SECOND_POST_ID).getLikesCount());
        repository.removeLike(SECOND_POST_ID);
        assertEquals(ZERO_LIKES_COUNT, repository.getPost(SECOND_POST_ID).getLikesCount(),
                "Likes count can't be negative");
    }

    @Test
    void testRemoveLike_negative() {
        assertThrows(DataAccessException.class, () -> repository.removeLike(null));
        assertThrows(DataAccessException.class, () -> repository.removeLike(INCORRECT_ID));
    }

    @Test
    void testDeletePost() {
        repository.deletePost(FIRST_POST_ID);
        assertTrue(repository.getAllPosts().stream().map(Post::getId).noneMatch(FIRST_POST_ID::equals));
        assertThrows(DataAccessException.class, () -> repository.getTags(FIRST_POST_ID));
        assertThrows(DataAccessException.class, () -> repository.getComments(FIRST_POST_ID));

    }

    @Test
    void testDeletePost_negative() {
        assertThrows(DataAccessException.class, () -> repository.deletePost(null));
        assertThrows(DataAccessException.class, () -> repository.deletePost(INCORRECT_ID));
    }

    @Test
    void testAddComment() {
        repository.addComment(FIRST_POST_ID, SECOND_COMMENT);
        assertTrue(repository.getComments(FIRST_POST_ID).stream().map(Comment::getText)
                .anyMatch(SECOND_COMMENT::equals));
    }

    @Test
    void testAddComment_negative() {
        assertThrows(DataAccessException.class, () -> repository.addComment(null, SECOND_COMMENT));
        assertThrows(DataAccessException.class, () -> repository.addComment(INCORRECT_ID, SECOND_COMMENT));
        assertThrows(DataAccessException.class, () -> repository.addComment(FIRST_POST_ID, null));
    }

    @Test
    void testDeleteComment() {
        repository.deleteComment(FIRST_COMMENT_ID);
        assertTrue(repository.getComments(FIRST_POST_ID).stream().map(Comment::getId)
                .noneMatch(FIRST_COMMENT_ID::equals));
    }

    @Test
    void testDeleteComment_negative() {
        assertThrows(DataAccessException.class, () -> repository.deleteComment(null));
        assertThrows(DataAccessException.class, () -> repository.deleteComment(INCORRECT_ID));
    }

    @Test
    void testUpdateComment() {
        repository.updateComment(FIRST_COMMENT_ID, NEW_COMMENT);
        assertTrue(repository.getComments(FIRST_POST_ID).stream().anyMatch(
                comment -> FIRST_COMMENT_ID.equals(comment.getId()) && NEW_COMMENT.equals(comment.getText())));
    }

    @Test
    void testUpdateComment_negative() {
        assertThrows(DataAccessException.class, () -> repository.updateComment(null, SECOND_COMMENT));
        assertThrows(DataAccessException.class, () -> repository.updateComment(INCORRECT_ID, SECOND_COMMENT));
        assertThrows(DataAccessException.class, () -> repository.updateComment(FIRST_COMMENT_ID, null));
    }


    @Test
    void testCreatePost() {
        UUID postId = repository.createPost(NEW_POST_NAME, NEW_POST_FULL_TEXT, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID);
        Post post = repository.getPost(postId);

        assertEquals(postId, post.getId());
        assertEquals(NEW_POST_NAME, post.getName());
        assertEquals(NEW_POST_FULL_TEXT, post.getFullText());
        assertEquals(NEW_POST_SHORT_TEXT, post.getShortText());
        assertEquals(ZERO_LIKES_COUNT, post.getLikesCount());
        assertEquals(NEW_IMAGE_ID, post.getImageId());
    }

    @Test
    void testCreatePost_negative() {
        assertThrows(DataAccessException.class,
                () -> repository.createPost(null, NEW_POST_FULL_TEXT, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class,
                () -> repository.createPost(NEW_POST_NAME, null, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class,
                () -> repository.createPost(NEW_POST_NAME, NEW_POST_FULL_TEXT, null, NEW_IMAGE_ID));
    }

    @Test
    void testUpdatePost() {
        repository.updatePost(FIRST_POST_ID, NEW_POST_NAME, NEW_POST_FULL_TEXT, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID);
        Post post = repository.getPost(FIRST_POST_ID);

        assertEquals(FIRST_POST_ID, post.getId());
        assertEquals(NEW_POST_NAME, post.getName());
        assertEquals(NEW_POST_FULL_TEXT, post.getFullText());
        assertEquals(NEW_POST_SHORT_TEXT, post.getShortText());
        assertEquals(NEW_IMAGE_ID, post.getImageId());
    }

    @Test
    void testUpdatePost_negative() {
        assertThrows(DataAccessException.class, () -> repository.updatePost(INCORRECT_ID, NEW_POST_NAME,
                NEW_POST_FULL_TEXT, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class, () -> repository.updatePost(null, NEW_POST_NAME, NEW_POST_FULL_TEXT,
                NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class, () -> repository.updatePost(FIRST_POST_ID, null,
                NEW_POST_FULL_TEXT, NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class, () -> repository.updatePost(FIRST_POST_ID, NEW_POST_NAME, null,
                NEW_POST_SHORT_TEXT, NEW_IMAGE_ID));
        assertThrows(DataAccessException.class, () -> repository.updatePost(FIRST_POST_ID, NEW_POST_NAME,
                NEW_POST_FULL_TEXT, null, NEW_IMAGE_ID));
    }

    @Test
    void testCreateTag() {
        repository.createTag(FIRST_POST_ID, SECOND_TAG_NAME);
        assertTrue(repository.getTags(FIRST_POST_ID).contains(SECOND_TAG_NAME));
    }

    @Test
    void testCreateTag_negative() {
        assertThrows(DataAccessException.class, () -> repository.createTag(null, SECOND_TAG_NAME));
        assertThrows(DataAccessException.class, () -> repository.createTag(INCORRECT_ID, SECOND_TAG_NAME));
        assertThrows(DataAccessException.class, () -> repository.createTag(FIRST_POST_ID, null));
        assertThrows(DataAccessException.class, () -> repository.createTag(FIRST_POST_ID, FIRST_TAG_NAME));
    }

    @Test
    void testDeleteTags() {
        assertDoesNotThrow(() -> repository.deleteTags(FIRST_POST_ID, null));
        assertDoesNotThrow(() -> repository.deleteTags(FIRST_POST_ID, Collections.emptyList()));
        assertDoesNotThrow(() -> repository.deleteTags(FIRST_POST_ID, Collections.singletonList(SECOND_TAG_NAME)));
        assertTrue(repository.getTags(FIRST_POST_ID).contains(FIRST_TAG_NAME));
        repository.deleteTags(FIRST_POST_ID, Collections.singletonList(FIRST_TAG_NAME));
        assertTrue(repository.getTags(FIRST_POST_ID).isEmpty());
    }

    @Test
    void testDeleteTags_negative() {
        assertThrows(DataAccessException.class, () -> repository.deleteTags(null,
                Collections.singletonList(FIRST_TAG_NAME)));
        assertThrows(DataAccessException.class, () -> repository.deleteTags(INCORRECT_ID,
                Collections.singletonList(FIRST_TAG_NAME)));
    }}
