package telran.forum.dao;

import telran.forum.model.Post;

import java.time.LocalDate;
import java.util.Arrays;

public class ForumImpl implements Forum {
    private Post[] posts;
    private int size;

    public ForumImpl() {
        int INITIAL_CAPACITY = 11;
        posts = new Post[INITIAL_CAPACITY];
    }

    @Override
    public boolean addPost(Post post) {
        if ( post == null || isPost(post) ) {
            return false;
        }
            // Бинарный поиск позиции для вставки
            int insertPos = Arrays.binarySearch(posts, 0, size, post);
            // Преобразуем в позицию вставки с использованием тернарного оператора
            insertPos = (insertPos < 0) ? -insertPos - 1 : insertPos;
            if (size < posts.length) {  // Если место есть в массиве
                                        // Сдвигаем элементы
                System.arraycopy(posts, insertPos, posts, insertPos + 1, size - insertPos);
            } else {  // Если массив заполнен, расширяем
                                    // Создаем новый массив, в два раза больше текущего
                Post[] newPosts = new Post[posts.length * 2];
                                    // Копируем элементы до позиции вставки
                System.arraycopy(posts, 0, newPosts, 0, insertPos);
                                    // Копируем оставшиеся элементы после вставки
                System.arraycopy(posts, insertPos, newPosts, insertPos + 1, size - insertPos);
                                    // Обновляем ссылку на новый массив
                posts = newPosts;
            }
                                        // и вставляем новый пост в нужную позицию
            posts[insertPos] = post;
                                      // Увеличиваем размер массива
            size++;
            return true;
    }

    private boolean isPost(Post post) {
        for(int i =0; i<size; i++){
            if(posts[i].equals(post)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removePost(int postId) {
        return false;
    }

    @Override
    public boolean updatePost(int postId, String content) {
        return false;
    }

    @Override
    public Post getPostById(int postId) {
        return null;
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        return new Post[0];
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
        return new Post[0];
    }

    @Override
    public int size() {
        return this.size;
    }

}
