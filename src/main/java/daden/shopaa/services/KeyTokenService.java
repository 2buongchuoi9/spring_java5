package daden.shopaa.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daden.shopaa.entity.KeyToken;
import daden.shopaa.repository.KeyTokenRepo;

@Service
public class KeyTokenService {
  @Autowired
  private KeyTokenRepo keyRepo;

  public boolean createKeyStore(String userId, String publicKey, String refresshToken) {

    try {
      Optional<KeyToken> oKey = keyRepo.findByUserId(userId);
      if (oKey.isPresent()) {
        KeyToken k = oKey.get();
        k.setPublicKey(publicKey);
        keyRepo.save(k);
      } else
        keyRepo.save(KeyToken.builder()
            .userId(userId)
            .publicKey(publicKey)
            .refreshToken(refresshToken)
            .refreshTokensUsed(null)
            .build());
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
