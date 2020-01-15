package uk.modl.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A very basic cache with a timeout mechanism.
 *
 * @param <K> The class that the keys will be instances of.
 * @param <V> The class that the values will be instances of.
 * @author tonywalmsley
 */
public class SimpleCache<K, V> {

    /**
     * The default cache timeout.
     */
    private static final int ONE_HOUR = 1000 * 60 * 60;

    /**
     * The actual cache is a HashMap of K,CacheEntry pairs.
     */
    private Map<K, CacheEntry<V>> cache = new HashMap<>();

    /**
     * The overridable cache timeout - defaults to 1 hour.
     */
    private int millis = ONE_HOUR;

    /**
     * Default Constructor
     */
    public SimpleCache() {
    }

    /**
     * Override the default cache timeout. Can be set at any time but only affects items cached after the value is set.
     *
     * @param millis The number of milliseconds until the cached item should be considered stale.
     */
    public void setTimeToLive(int millis) {
        this.millis = millis;
    }

    /**
     * Check whether the cache contains a key
     *
     * @param key the key to check
     * @return true if the cache contains the key
     */
    public boolean contains(final K key) {
        return get(key) != null;
    }

    /**
     * Add an item to the cache.
     *
     * @param key   The key of class K
     * @param value The value of class V
     */
    public void put(K key, V value) {
        final CacheEntry<V> entry = new CacheEntry<>(value, millis + System.currentTimeMillis());
        cache.put(key, entry);
    }

    /**
     * Get an item from the cache.
     *
     * @param key The key of class K
     * @return null if the item is not present or has expired, otherwise the value of class V
     */
    public V get(K key) {
        final CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.expiry < System.currentTimeMillis()) {

            // Remove the expired item
            if (entry != null) {
                cache.remove(key);
            }
            return null;
        }
        return entry.value;
    }

    /**
     * The CacheEntry holds the cached value and the expiry time of the item.
     *
     * @param <V> The class of the values to be cached.
     */
    private static class CacheEntry<V> {
        private V value;
        private long expiry;

        /**
         * Constructor.
         *
         * @param value  The value to be cached.
         * @param expiry The expiry time of the cached item.
         */
        CacheEntry(V value, long expiry) {
            this.value = value;
            this.expiry = expiry;
        }
    }
}