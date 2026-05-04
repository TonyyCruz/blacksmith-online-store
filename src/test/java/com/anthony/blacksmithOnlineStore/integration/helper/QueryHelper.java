package com.anthony.blacksmithOnlineStore.integration.helper;

public class QueryHelper {

  public static String buildQueryString(Object dto) {
    StringBuilder queryString = new StringBuilder("?");
    var fields = dto.getClass().getRecordComponents();
    for (var field : fields) {
      try {
        Object value = field.getAccessor().invoke(dto);
        if (value != null) {
          queryString.append(field.getName()).append("=").append(value).append("&");
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to build query string", e);
      }
    }
    // Remove the trailing '&' if it exists
    if (queryString.length() > 1) {
      queryString.deleteCharAt(queryString.length() - 1);
    }
    return queryString.toString();
  }
}
