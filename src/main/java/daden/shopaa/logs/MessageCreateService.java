package daden.shopaa.logs;

import org.springframework.stereotype.Service;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateService extends MessageListener implements EventListener<MessageCreateEvent> {

  @Override
  public Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }

  @Override
  public Mono<Void> execute(MessageCreateEvent event) {
    return processCommand(event.getMessage());
  }
}