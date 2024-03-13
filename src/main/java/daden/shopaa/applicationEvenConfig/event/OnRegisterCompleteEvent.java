package daden.shopaa.applicationEvenConfig.event;

import org.springframework.context.ApplicationEvent;

import daden.shopaa.entity.User;

public class OnRegisterCompleteEvent extends ApplicationEvent {
  private User user;

  public User getUser() {
    return user;
  }

  OnRegisterCompleteEvent(User user) {
    super(user);
    this.user = user;
  }
}
