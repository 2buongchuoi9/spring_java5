package daden.shopaa.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import daden.shopaa.logs.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

import discord4j.core.event.domain.Event;

@Configuration
public class LogDiscordConfig {
  @Value("${den.log.discord.token}")
  private String token;

  @Bean
  public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
    final GatewayDiscordClient client = DiscordClientBuilder.create(token)
        .build()
        .login()
        .block();

    for (EventListener<T> listener : eventListeners) {
      client.on(listener.getEventType())
          .flatMap(listener::execute)
          .onErrorResume(listener::handleError)
          .subscribe();
    }

    return client;
  }
}
