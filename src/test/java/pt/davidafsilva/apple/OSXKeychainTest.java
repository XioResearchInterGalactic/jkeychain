package pt.davidafsilva.apple;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
public class OSXKeychainTest {

  private static final String SERVICE = "jkeychain_service";
  private static final String KEY = "jkeychain_key";
  private static final String INITIAL_PASSWORD = "jkeychain_password_v1";
  private static final String UPDATED_PASSWORD = "jkeychain_password_v2";

  private static OSXKeychain keychain;

  @BeforeAll
  static void setup() {
    initKeychain();
  }

  @Test
  @Order(1)
  public void addGenericPassword() {
    assertDoesNotThrow(
        () -> keychain.addGenericPassword(SERVICE, KEY, INITIAL_PASSWORD),
        "failed to add a generic password"
    );
  }

  @Test
  @Order(2)
  public void findInitialGenericPassword() {
    String pass = assertDoesNotThrow(
        () -> keychain.findGenericPassword(SERVICE, KEY).orElse(null),
        "Failed to retrieve generic password"
    );
    assertEquals(INITIAL_PASSWORD, pass, "retrieved password did not match");
  }

  @Test
  @Order(3)
  public void modifyGenericPassword() {
    assertDoesNotThrow(
        () -> keychain.modifyGenericPassword(SERVICE, KEY, UPDATED_PASSWORD),
        "failed to update a generic password"
    );
  }

  @Test
  @Order(4)
  public void findUpdatedGenericPassword() {
    String pass = assertDoesNotThrow(
        () -> keychain.findGenericPassword(SERVICE, KEY).orElse(null),
        "Failed to retrieve generic password"
    );
    assertEquals(UPDATED_PASSWORD, pass, "retrieved password did not match");
  }

  @Test
  @Order(5)
  public void deleteGenericPassword() {
    assertDoesNotThrow(
        () -> keychain.deleteGenericPassword(SERVICE, KEY),
        "failed to delete a generic password"
    );
  }

  private static void initKeychain() {
    keychain = assertDoesNotThrow(OSXKeychain::getInstance, "failed to initialize keychain");
  }
}
