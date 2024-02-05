package daden.shopaa.config;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daden.shopaa.entity.User;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.utils._enum.RoleShopEnum;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
  private final UserRepo userRepo;

  @Value("${den.date-delete-user-mod}")
  private Integer DAY;

  /**
   * delete user has role MOD 'DAY'
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void removeModUsers() {
    LocalDateTime dayDelete = LocalDateTime.now().minusDays(DAY);
    Set<User> modUsers = userRepo.findByRoles(Set.of(RoleShopEnum.MOD));

    modUsers.stream().forEach(v -> {
      if (v.getDateCreate().isBefore(LocalDateTime.from(dayDelete)))
        userRepo.delete(v);
    });
  }
}
