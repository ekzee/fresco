package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.image.EncodedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Lazily initializes {@link NetworkFetcher} through provided {@link NetworkFetcherFactory}
 * when any of the {@link NetworkFetcher} methods are called.
 */
public class LazyNetworkFetcher<FETCH_STATE extends FetchState> implements NetworkFetcher<FETCH_STATE> {

  private final NetworkFetcherFactory<FETCH_STATE> networkFetcherFactory;
  private NetworkFetcher<FETCH_STATE> fetcher;

  public LazyNetworkFetcher(@Nonnull NetworkFetcherFactory<FETCH_STATE> networkFetcherFactory) {
    this.networkFetcherFactory = networkFetcherFactory;
  }

  @Override
  public FETCH_STATE createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
    return getFetcher().createFetchState(consumer, producerContext);
  }

  @Override
  public void fetch(FETCH_STATE fetchState, Callback callback) {
    getFetcher().fetch(fetchState, callback);
  }

  @Override
  public boolean shouldPropagate(FETCH_STATE fetchState) {
    return getFetcher().shouldPropagate(fetchState);
  }

  @Override
  public void onFetchCompletion(FETCH_STATE fetchState, int byteSize) {
    getFetcher().onFetchCompletion(fetchState, byteSize);
  }

  @Nullable
  @Override
  public Map<String, String> getExtraMap(FETCH_STATE fetchState, int byteSize) {
    return getFetcher().getExtraMap(fetchState, byteSize);
  }

  private NetworkFetcher<FETCH_STATE> getFetcher() {
    if (fetcher == null) {
      synchronized (this) {
        if (fetcher == null) {
          fetcher = networkFetcherFactory.create();
        }
      }
    }
    return fetcher;
  }
}
