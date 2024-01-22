package daden.shopaa.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Infos")
public class Info {
  @Id
  private String id;

  private String logo;

  private String[] banners;

}

// info_logo: { type: String },
// info_follow: { type: Array },
// info_service_customer: { type: Array },
// info_about: { type: Array },
// info_payment: { type: Array },
// info_ship: { type: Array },
// info_banner: { type: Array },
