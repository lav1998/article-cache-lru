Article Cache — Backend Design Problem (Publicis Sapient R2)
Context
You are building an in-memory Article Cache for a news feed service. The service fetches article metadata from a slow content database. To reduce latency, you need a fixed-size cache split into two segments that rewards repeated access — articles seen once sit in a probationary zone, while articles accessed again earn protected status and are harder to evict.
This is the same strategy used by MySQL InnoDB's buffer pool.

The Article Model

public class Article {
	private String articleId;
	private String headline;
	private String category;	// e.g., "SPORTS", "FINANCE", "TECH"
	private long publishedAt;   // epoch millis
	private boolean isBreaking; // whether it's a breaking news article
}

Cache Structure
The cache has a total fixed capacity set at conystruction time, split into two segments:

Probationary segment — holds 2/3 of total capacity (rounded down)
Protected segment — holds 1/3 of total capacity (rounded up)

Both segments behave as LRU lists internally.

Eviction Policy
New articles always enter the probationary segment
An article accessed again (via get) graduates from probationary → protected
If the protected segment is full when a graduation happens, its LRU entry is demoted back to probationary before the promotion
Eviction on capacity breach always targets the LRU entry in the probationary segment
Articles already in the protected segment are never directly evicted — they must be demoted first

Operations to Implement
1. Article get(String articleId)

Return the article if present, null if not
If found in probationary — graduate it to protected (demote protected's LRU first if protected is full)
If found in protected — move it to the MRU end of the protected list (standard LRU refresh)

2. void put(String articleId, Article article)
Two behaviours:
New article: insert into the probationary segment at MRU end. If probationary is at capacity, evict its LRU entry first
Existing article: treat as an access — same promotion/demotion logic as get, then update the stored data

3. void evict() (internal)
Called by put on capacity breach. Always evicts the LRU entry from the probationary segment. 




Add UTs
