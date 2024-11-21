package telran.forum.model;

import java.time.LocalDateTime;

public class Post implements Comparable<Post>{
    private final int postId;
    private String title;
    private final String author;
    private String content;
    private LocalDateTime date;
    private int likes;

    public Post(int postId, String title, String author, String content) {
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public int getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getLikes() {
        return likes;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int addLike() {
        return ++this.likes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;

        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return postId;
    }

    @Override
    public String toString() {
        return  "[ " + postId + " ]" + "< " + author + " >" +
                "\"" + title + "\"\n" +
                "{ " + content + " }\n" +
                ", date: " + date +
                ", likes: " + likes;
    }

    @Override
    public int compareTo(Post post) {
        int res = this.author.toUpperCase().compareTo(post.author.toUpperCase());
        res = res!= 0 ? res : this.title.toUpperCase().compareTo(post.title.toUpperCase());
        res = res!= 0 ? res : this.date.compareTo(post.date);
        res = res!=0 ? res : Integer.compare(this.postId, post.postId);
        return res;
    }
}
