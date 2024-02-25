package daden.shopaa.entity.listent;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import daden.shopaa.entity.Order;

@Component
@SuppressWarnings("null")
public class OrderListener extends AbstractMongoEventListener<Order> {

  // @Override
  // public void onBeforeSave(BeforeSaveEvent<Order> event) {
  // Order order = event.getSource();
  // if (order.isNew()) {
  // order.setCreateDate(LocalDateTime.now());
  // }
  // super.onBeforeSave(event);
  // }

  // @Override
  // public void onAfterSave(AfterSaveEvent<Order> event) {
  // super.onAfterSave(event);
  // Order order = event.getSource();
  // if (order.isNew()) {
  // order.setCreateDate(LocalDateTime.now());
  // }
  // }

}
