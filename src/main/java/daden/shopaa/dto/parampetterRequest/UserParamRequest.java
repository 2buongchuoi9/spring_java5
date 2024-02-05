package daden.shopaa.dto.parampetterRequest;

import java.util.ArrayList;
import java.util.List;

import daden.shopaa.utils._enum.AuthTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserParamRequest {
  private String keySearch;
  private Boolean status;
  private AuthTypeEnum authType;
  private Boolean verify;

  public String getMessage() {
    List<String> params = new ArrayList<>();

    if (keySearch != null && !keySearch.isEmpty())
      params.add("keySearch: " + keySearch);

    if (status != null)
      params.add("status: " + status);

    if (authType != null)
      params.add("authType: " + authType);

    if (verify != null)
      params.add("verify: " + verify);

    return params.isEmpty() ? "success" : "success with param: " + String.join(", ", params);
  }
}
