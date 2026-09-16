package model;

public class Node {
    String key;
    Article value;
    Node next;
    Node prev;
    boolean inProtected;
    public Node(String articleId, Article article) {
        this.key = articleId;
        this.value = article;
        this.inProtected = false;
    }

    public boolean isInProtected() { return this.inProtected; }
    public void setInProtected(boolean inProtected) { this.inProtected = inProtected; }
    public Article getValue() { return this.value; }
    public String getKey() { return this.key; }
    public void setValue(Article value) { this.value = value; }
}
