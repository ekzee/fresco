package com.facebook.imagepipeline.producers;

/**
 * Factory for creating {@link NetworkFetcher}, useful for lazy initialization.
 */
public interface NetworkFetcherFactory<FETCH_STATE extends FetchState> {
  NetworkFetcher<FETCH_STATE> create();
}
