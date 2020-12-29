package com.deltareporter.client;

import com.deltareporter.util.AsyncUtil;
import com.deltareporter.util.ConfigurationUtil;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public enum DeltaSingleton {
  INSTANCE;

  private DeltaClient deltaClient;
  private final CompletableFuture<Integer> INIT_FUTURE;

  DeltaSingleton() {
    this.INIT_FUTURE =
        CompletableFuture.supplyAsync(
            () -> {
              Integer result = null;

              try {
                Properties config = ConfigurationUtil.getConfigurationFile();

                boolean enabled = Boolean.parseBoolean(config.getProperty("delta_enabled"));

                String url = config.getProperty("delta_service_url");

                this.deltaClient = new DeltaClient(url);
                if (enabled && this.deltaClient.isAvailable()) {
                  result = 1;
                }
              } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
              }

              return result;
            });
  }

  public DeltaClient getClient() {
    return isRunning() ? this.deltaClient : null;
  }

  public boolean isRunning() {
    Integer response;
    try {
      response = AsyncUtil.getAsync(this.INIT_FUTURE, "Cannot connect to Delta Reporter");
    } catch (Exception e) {
      return false;
    }
    return (response != null);
  }
}
