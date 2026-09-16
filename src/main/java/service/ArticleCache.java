package service;

import model.Article;
import model.DLL;
import model.Node;

import java.util.HashMap;
import java.util.Map;

public class ArticleCache {
    private final int probationaryCapacity;
    private final int protectedCapacity;
    private final Map<String, Node> map;
    private final DLL probationaryDLL;
    private final DLL protectedDLL;

    public ArticleCache(int capacity) {
        this.probationaryCapacity = Math.floorDiv(2*capacity, 3);
        this.protectedCapacity = Math.ceilDiv(capacity, 3);
        this.map = new HashMap<>(capacity);
        this.probationaryDLL = new DLL(this.probationaryCapacity);
        this.protectedDLL = new DLL(this.protectedCapacity);
    }

    public Article get(String articleId) {
        Node node = map.get(articleId);
        if(node == null) return null;
        if(node.isInProtected()) {
            protectedDLL.removeNode(node);
            protectedDLL.insertAtMRU(node);
        }
        else {
            promote(node);
        }
        return node.getValue();
    }

    private void promote(Node node) {
        if(protectedDLL.isFull()) {
            Node demoted = protectedDLL.removeLRU();
            demoted.setInProtected(false);
            probationaryDLL.insertAtMRU(demoted);
        }
        probationaryDLL.removeNode(node);
        protectedDLL.insertAtMRU(node);
        node.setInProtected(true);
    }

    public void put(String articleId, Article article) {
        Node node = map.get(articleId);

        if(node == null) {
            if(probationaryDLL.isFull()) {
                evict();
            }
            Node newNode = new Node(articleId, article);
            map.put(articleId, newNode);
            probationaryDLL.insertAtMRU(newNode);
            return;
        }

        if(node.isInProtected()) {
            protectedDLL.removeNode(node);
            protectedDLL.insertAtMRU(node);
        }
        else {
            promote(node);
        }
        node.setValue(article);
    }

    private void evict() {
        Node evicted = probationaryDLL.removeLRU();
        if(evicted != null) {
            map.remove(evicted.getKey());
        }
    }
}