package telran.forum.dao;

import telran.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class ForumImpl implements Forum {
    private Post[] posts;
    private int size;
    private IndexEntry[] indexEntries;
    public static Comparator<Post> comparator = (Post p1, Post p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        res = res != 0 ? res : p1.getDate().compareTo(p2.getDate());
        return res != 0 ? res : Integer.compare(p1.getPostId(), p2.getPostId());
    };

    public ForumImpl() {
        int initialCapacity = 11;
        posts = new Post[initialCapacity];
        indexEntries = new IndexEntry[initialCapacity];
    }

    private boolean putIndexEntry(int postId, int index){
        return true;
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

    private int findIndexByPostId(int postId){
        for(int i = 0; i <size; i++){
            if(posts[i].getPostId() == postId)
                return i;
        }
        return -1;
    }


    @Override
    public boolean removePost(int postId) {
        int indexForRemove = findIndexByPostId(postId);
        if( indexForRemove < 0 ) { return false;}
        System.arraycopy(posts, indexForRemove+1, posts, indexForRemove, size-indexForRemove-1);
        size--;
        return true;
    }

    @Override
    public boolean updatePost(int postId, String content) {
        int indexForUpdate = findIndexByPostId(postId);
        if(  indexForUpdate < 0 ) { return false;}
        posts[ indexForUpdate ].setContent(content);
        return true;
    }

    @Override
    public Post getPostById(int postId) {
        int index = findIndexByPostId(postId);
     //   if(  index < 0 ) { return null;}
        return index < 0 ? null : posts[index];
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        for(int i=0; i<size; i++){
            System.out.println(posts[i]);
        }

       Post pattern = new Post(Integer.MIN_VALUE, "", author, null);
       pattern.setDate(LocalDateTime.MIN);
       System.out.println(pattern);
       int from = - Arrays.binarySearch(posts, 0, size, pattern) -1;
        System.out.println("from= " + from);
       pattern = new Post(Integer.MAX_VALUE, "", author, null);
       pattern.setDate(LocalDateTime.MAX);
        System.out.println(pattern);
       int to = - Arrays.binarySearch(posts, 0, size, pattern) -1;
        System.out.println("to= " + to);
       return Arrays.copyOfRange(posts, from , to);
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
        return new Post[0];
    }

    private Post[] postsByPredicateThrowIndex(Predicate<Post> predicate) {
        int iTrue = 0;
        int[] indexes = new int[size];
        for (int index = 0; index < size; index++) {
            if (predicate.test(posts[index])) {
                indexes[iTrue++] = index;
            }
        }
        Post[] result = new Post[iTrue];
        for (int i = 0; i < iTrue; i++) {
            result[i] = this.posts[indexes[i]];
        }
        return result;
    }


    @Override
    public int size() {
        return this.size;
    }

}
