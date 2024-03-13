package daden.shopaa.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.entity.RestChannel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscordService {
  private final GatewayDiscordClient discordClient;

  @Value("${den.log.discord.channelId}")
  private String channelId;

  public void sendMessage(String messageContent) {
    try {
      discordClient.getChannelById(Snowflake.of("1215708874204647434"))
          .cast(MessageChannel.class)
          .flatMap(channel -> ((RestChannel) channel).createMessage(messageContent))
          .block();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
