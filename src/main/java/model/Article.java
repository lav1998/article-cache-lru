package model;

public class Article {
    private String articleId;
    private String headline;
    private String category;	// e.g., "SPORTS", "FINANCE", "TECH"
    private long publishedAt;   // epoch millis
    private boolean isBreaking; // whether it's a breaking news article

    public Article(String articleId, String headline, String category, boolean isBreaking) {
        this.articleId = articleId;
        this.headline = headline;
        this.category = category;
        this.publishedAt = System.currentTimeMillis();
        this.isBreaking = isBreaking;
    }

    public String getArticleId() {
        return articleId;
    }
}
