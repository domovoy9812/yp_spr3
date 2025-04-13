package ru.yandex.practicum.bliushtein.spr4.controller.configuration;

import ru.yandex.practicum.bliushtein.spr4.service.dto.CommentInfo;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostDetails;
import ru.yandex.practicum.bliushtein.spr4.service.dto.PostSummary;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestData {
    public final static String[] ENPTY_STRING_ARRAY = new String[0];

    public final static UUID POST_ID = new UUID(0, 0);
    public final static UUID COMMENT_ID = new UUID(0, 1);
    public final static UUID IMAGE_ID = new UUID(0, 2);
    public final static String COMMENT_TEXT = "some new comment";
    public final static String SEARCH_TAG = "tag to search";
    public final static String SEARCH_TAG_2 = "tag to search 2";

    public final static List<PostSummary> POST_SUMMARY_LIST_SIZE_6 = generatePostSummaries(1,6);

    public final static List<PostSummary> POST_SUMMARY_LIST_SIZE_1 = generatePostSummaries(7,1);

    public final static PostDetails POST_DETAILS = generatePostDetails();

    private static PostDetails generatePostDetails() {
        return new PostDetails(new UUID(0, 3), "post details name", "post details short text",
                "post details full text", ZonedDateTime.now(), 0, List.of("tag1", "tag2"),
                Collections.singletonList(new CommentInfo(COMMENT_ID, "post details comment text",
                        ZonedDateTime.now())), IMAGE_ID);
    }

    public static List<PostSummary> generatePostSummaries(int start, int count) {
        return IntStream.iterate(start, i -> i + 1).limit(count).mapToObj(
                i -> new PostSummary(new UUID(1, i), "post name " + i, "short text " + i,
                        ZonedDateTime.now(), i, Collections.singletonList("tag" + i), i, new UUID(2, i))
        ).collect(Collectors.toList());
    }
}
