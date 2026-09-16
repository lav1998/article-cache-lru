import model.Article;
import service.ArticleCache;

public class Main {
    public static void main(String[] args) {
        ArticleCache cache = new ArticleCache(3); // probationary=2, protected=1

        cache.put("a1", new Article("a1", "Headline A1", "TECH", false));
        cache.put("a2", new Article("a2", "Headline A2", "SPORTS",  false));

        System.out.println(cache.get("a1")); // hit -> a1 graduates to protected
        System.out.println(cache.get("a1").getArticleId()); // "a1"

        cache.put("a3", new Article("a3", "Headline A3", "FINANCE", true));
        // probationary now: [a2, a3], protected: [a1]

        cache.put("a4", new Article("a4", "Headline A4", "TECH", false));
        // capacity breach -> evicts a2 (probationary LRU)

        System.out.println(cache.get("a2")); // expect null (evicted)
        System.out.println(cache.get("a3"));  // expect present
        System.out.println(cache.get("a1"));  // expect present, safe in protected
    }
}