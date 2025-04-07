package ru.yandex.practicum.bliushtein.spr3.service.impl.configuration;

import ru.yandex.practicum.bliushtein.spr3.data.model.Comment;
import ru.yandex.practicum.bliushtein.spr3.data.model.Post;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TestData {
    public final static Post POST = new Post(new UUID(0, 0), "post name",
            """
                    post first line
                    post second line
                    """, "post first line", ZonedDateTime.now(), 0,
            new UUID(0, 1));

    public final static List<Post> POST_LIST = Collections.singletonList(POST);

    public final static String TAG_1 = "some tag 1";
    public final static String TAG_2 = "some tag 2";
    public final static List<String> TAGS_1 = Collections.singletonList(TAG_1);
    public final static List<String> TAGS_2 = Collections.singletonList(TAG_2);
    public final static int COMMENTS_COUNT = 1;

    public final static Comment COMMENT = new Comment(new UUID(0, 2), "comment text",
            ZonedDateTime.now(), POST.getId());
    public final static List<Comment> COMMENTS = Collections.singletonList(COMMENT);
}
