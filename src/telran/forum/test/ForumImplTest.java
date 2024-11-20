import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.forum.dao.Forum;
import telran.forum.dao.ForumImpl;
import telran.forum.model.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ForumImplTest {
    private Forum forum;
    private final Post[] testPosts = new Post[10];

    @BeforeEach
    void setUp() {
        forum = new ForumImpl(); // Инициализация тестируемого объекта

        // Создаем 10 постов с двумя авторами и разными датами
        for (int i = 1; i <= 10; i++) {
            String author = (i % 2 == 0) ? "author1" : "author2";
            testPosts[i - 1] = new Post(i, author, "Title " + i, "Content " + i);
            testPosts[i - 1].setDate(LocalDateTime.now().minusDays(i)); // Устанавливаем дату через setDate
            forum.addPost(testPosts[i - 1]);
        }
    }

    @Test
    void addPost() {
        Post newPost = new Post(11, "newAuthor", "New Title", "New Content");
        newPost.setDate(LocalDateTime.now()); // Устанавливаем текущую дату
        assertTrue(forum.addPost(newPost), "addPost should return true for a new post");
        assertEquals(11, forum.size(), "Forum size should increase after adding a new post");
        assertEquals(newPost, forum.getPostById(11), "Added post should be retrievable by its ID");
        assertFalse(forum.addPost(newPost));
        assertFalse(forum.addPost(testPosts[3]));
        assertFalse(forum.addPost(null));
    }

    @Test
    void removePost() {
        assertTrue(forum.removePost(1), "removePost should return true for an existing post");
        assertEquals(9, forum.size(), "Forum size should decrease after removing a post");
        assertNull(forum.getPostById(1), "Removed post should not be retrievable by its ID");
        // Проверяем, что повторное удаление возвращает false
        assertFalse(forum.removePost(1), "Removing a non-existent post should return false");
        // Удаляем все остфвшиеся посты, добавленные в @BeforeEach
        for (int i = 2; i <= 10; i++) {
            assertTrue(forum.removePost(i), "Post with ID " + i + " should be successfully removed");
            assertNull(forum.getPostById(i), "Removed post with ID " + i + " should not be retrievable by its ID");
        }
        // Проверяем, что все посты удалены, и размер форума равен 0
        assertEquals(0, forum.size(), "Forum size should be 0 after removing all posts");
        // Проверяем, что попытка удалить несуществующий пост возвращает false
        int nonExistentPostId = 999;
        assertFalse(forum.removePost(nonExistentPostId),
                "Attempt to remove a non-existent post should return false");
    }

    @Test
    void testUpdatePost() {
        int postId = 3;
        String newContent = "Updated Content";

        // Проверяем, что обновление возвращает true
        assertTrue(forum.updatePost(postId, newContent), "updatePost should return true for an existing post");

        // Проверяем, что содержимое поста обновлено
        Post updatedPost = forum.getPostById(postId);
        assertNotNull(updatedPost, "Post should exist after being updated");
        assertEquals(newContent, updatedPost.getContent(), "Post content should match the updated content");

        // Проверяем, что обновление не работает для несуществующего поста
        assertFalse(forum.updatePost(99, "Some Content"), "updatePost should return false for a non-existent post");
    }


    @Test
    void testGetPostById() {
        Post post = forum.getPostById(5);
        assertNotNull(post, "getPostById should return a post if it exists");
        assertEquals(5, post.getPostId(), "Post ID should match the requested ID");
        assertEquals(testPosts[4], post);
        Post expectedPost = testPosts[4]; // testPosts[4] соответствует ID 5

        assertEquals(expectedPost.getPostId(), post.getPostId(), "IDs should match");
        assertEquals(expectedPost.getAuthor(), post.getAuthor(), "Authors should match");
        assertEquals(expectedPost.getContent(), post.getContent(), "Content should match");
        assertEquals(expectedPost.getDate(), post.getDate(), "Dates should match");
        assertEquals(expectedPost.getLikes(), post.getLikes(), "Likes count should match");
        assertEquals(expectedPost.getTitle(), post.getTitle(), "Likes count should match");
    }

    @Test
    void getPostsByAuthor() {
        Post[] posts = forum.getPostsByAuthor("author1");
        assertEquals(5, posts.length, "There should be five posts by this author");
        for (Post post : posts) {
            assertEquals("author1", post.getAuthor(), "Author of the post should match the requested author");
        }
        Post[] postsAuthor1 = new Post[5];
        for (int i = 0, j = 1; i < 5; i++, j = j + 2) {
            postsAuthor1[i] = testPosts[j];
        }
        assertArrayEquals(postsAuthor1, posts);
        posts = forum.getPostsByAuthor("author100");
        postsAuthor1 = new Post[0];
        assertNotNull(posts, "Method should not return null for an unknown author");
        assertEquals(0, posts.length, "There should be no posts for an unknown author");
        assertArrayEquals(postsAuthor1, posts);
    }

    @Test
    void testGetPostsByAuthorWithDateRange() {
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(5);
        LocalDateTime dateTo = LocalDateTime.now();
        Post[] posts = forum.getPostsByAuthor("author2", dateFrom.toLocalDate(), dateTo.toLocalDate());
        assertEquals(3, posts.length, "There should be three posts by this author in the date range");
        for (Post post : posts) {
            assertEquals("author2", post.getAuthor(), "Author of the post should match the requested author");
            assertTrue(post.getDate().isAfter(dateFrom.minusSeconds(1)) &&
                    post.getDate().isBefore(dateTo.plusSeconds(1)), "Post date should be within the range");
        }
    }

    @Test
    void size() {
        assertEquals(testPosts.length, forum.size());
    }
}