/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
    private static final int SEVEN_DAYS = 1000 * 60 * 60 * 24 * 7;

    /**
     * The actual cache is a HashMap of K,CacheEntry pairs.
     */
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();

    /**
     * Default Constructor
     */
    public SimpleCache() {
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
        final CacheEntry<V> entry = new CacheEntry<>(value, SEVEN_DAYS + System.currentTimeMillis());
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

        private final V value;

        private final long expiry;

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