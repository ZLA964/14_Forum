package telran.forum.dao;

public class IndexEntry implements Comparable<IndexEntry>{
    private int postId;
    private int index;

    public IndexEntry(int postId, int index) {
        this.postId = postId;
        this.index = index;
    }

    @Override
    public int compareTo(IndexEntry o) {
        return Integer.compare(this.postId, o.postId);
    }
}
