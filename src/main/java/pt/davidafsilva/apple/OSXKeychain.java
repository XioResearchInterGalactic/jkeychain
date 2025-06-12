package pt.davidafsilva.apple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An interface to the OS X Keychain. The names of functions and parameters
 * will mostly match the functions listed in the <a href="http://developer.apple.com/library/mac/#documentation/Security/Reference/keychainservices/Reference/reference.html">Keychain
 * Services Reference</a>.
 *
 * @author Conor McDermottroe
 */
public class OSXKeychain {

  /**
   * The singleton instance of the keychain. Lazily loaded in
   * {@link #getInstance()}.
   */
  private static OSXKeychain instance;

  private static final String libraryName = "osxkeychain";
  private static final String libraryExtension = ".dylib";
  private static final String libraryNameWithExtension = libraryName + libraryExtension;

  /**
   * Prevent this class from being instantiated directly.
   */
  private OSXKeychain() {
  }

  /**
   * Get an instance of the keychain.
   *
   * @return An instance of this class.
   * @throws OSXKeychainException If it's not possible to connect to the
   *                              keychain.
   */
  public static OSXKeychain getInstance() throws pt.davidafsilva.apple.OSXKeychainException {
    if (instance == null) {
      try {
        loadSharedObject();
      } catch (IOException e) {
        throw new pt.davidafsilva.apple.OSXKeychainException("Failed to load " + libraryNameWithExtension, e);
      }
      instance = new OSXKeychain();
    }
    return instance;
  }

  /**
   * Add a non-internet password to the keychain.
   *
   * @param serviceName The name of the service the password is
   *                    for.
   * @param accountName The account name/username for the
   *                    service.
   * @param password    The password for the service.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  public void addGenericPassword(String serviceName, String accountName, String password)
      throws pt.davidafsilva.apple.OSXKeychainException {
    _addGenericPassword(serviceName, accountName, password);
  }

  /**
   * Update an existing non-internet password to the keychain.
   *
   * @param serviceName The name of the service the password is
   *                    for.
   * @param accountName The account name/username for the
   *                    service.
   * @param password    The password for the service.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  public void modifyGenericPassword(String serviceName, String accountName, String password)
      throws pt.davidafsilva.apple.OSXKeychainException {
    _modifyGenericPassword(serviceName, accountName, password);
  }

  /**
   * Finds an existing non-internet password in the keychain
   * @param serviceName The name of the service the password is
   *                    for.
   * @param accountName The account name/username for the
   *                    service.
   * @return the password for the service or an empty optional
   * @throws pt.davidafsilva.apple.OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  public Optional<String> findGenericPassword(String serviceName, String accountName)
      throws pt.davidafsilva.apple.OSXKeychainException {
    return Optional.ofNullable(_findGenericPassword(serviceName, accountName));
  }

  /**
   * Delete a generic password from the keychain.
   *
   * @param serviceName The name of the service the password is
   *                    for.
   * @param accountName The account name/username for the
   *                    service.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  public void deleteGenericPassword(String serviceName, String accountName)
      throws pt.davidafsilva.apple.OSXKeychainException {
    _deleteGenericPassword(serviceName, accountName);
  }

  /* ************************* */
  /* JNI stuff from here down. */
  /* ************************* */

  /**
   * See Java_pt_davidafsilva_ghn_service_options_osx_OSXKeychain__1addGenericPassword for
   * the implementation of this and use {@link #addGenericPassword(String,
   * String, String)} to call this.
   *
   * @param serviceName The value which should be passed as the
   *                    serviceName parameter to
   *                    SecKeychainAddGenericPassword.
   * @param accountName The value which should be passed as the
   *                    accountName parameter to
   *                    SecKeychainAddGenericPassword.
   * @param password    The value which should be passed as the
   *                    password parameter to
   *                    SecKeychainAddGenericPassword.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  private native void _addGenericPassword(String serviceName, String accountName, String password)
      throws pt.davidafsilva.apple.OSXKeychainException;

  /**
   * See Java_pt_davidafsilva_ghn_service_options_osx_OSXKeychain__1modifyGenericPassword for
   * the implementation of this and use {@link #modifyGenericPassword(String,
   * String, String)} to call this.
   *
   * @param serviceName The value which should be passed as the
   *                    serviceName parameter to
   *                    SecKeychainAddGenericPassword.
   * @param accountName The value which should be passed as the
   *                    accountName parameter to
   *                    SecKeychainAddGenericPassword.
   * @param password    The value which should be passed as the
   *                    password parameter to
   *                    SecKeychainAddGenericPassword.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  private native void _modifyGenericPassword(String serviceName, String accountName,
      String password) throws pt.davidafsilva.apple.OSXKeychainException;

  /**
   * See Java_pt_davidafsilva_ghn_service_options_osx_OSXKeychain__1findGenericPassword for
   * the implementation of this and use {@link #findGenericPassword(String,
   * String)} to call this.
   *
   * @param serviceName The value which should be passed as the
   *                    serviceName parameter to
   *                    SecKeychainFindGenericPassword.
   * @param accountName The value for the accountName parameter
   *                    to SecKeychainFindGenericPassword.
   * @return The first password which matches the
   * details supplied.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  private native String _findGenericPassword(String serviceName, String accountName)
      throws pt.davidafsilva.apple.OSXKeychainException;

  /**
   * See Java_pt_davidafsilva_ghn_service_options_osx_OSXKeychain__1deleteGenericPassword for
   * the implementation of this and use {@link #deleteGenericPassword(String,
   * String)} to call this.
   *
   * @param serviceName The value which should be passed as the
   *                    serviceName parameter to
   *                    SecKeychainFindGenericPassword in order
   *                    to find the password to delete it.
   * @param accountName The value for the accountName parameter
   *                    to SecKeychainFindGenericPassword in
   *                    order to find the password to delete it.
   * @throws OSXKeychainException If an error occurs when communicating
   *                              with the OS X keychain.
   */
  private native void _deleteGenericPassword(String serviceName, String accountName)
      throws pt.davidafsilva.apple.OSXKeychainException;

  /**
   * Load the shared object which contains the implementations for the native
   * methods in this class.
   *
   * @throws IOException If the shared object could not be loaded.
   */
  private static void loadSharedObject() throws IOException {

    String appDir = System.getProperty("app.dir");
    if (appDir != null) {
      // && means app.dir
      // Windows: -Djava.library.path=&&;&&..\bin
      // macOS:   -Djava.library.path=&&/../runtime/Contents/Home/lib:&&
      String os = System.getProperty("os.name").toLowerCase();
      if (os.contains("win")) {
        if (tryLoadLibrary(appDir, libraryNameWithExtension)) return;
        if (tryLoadLibrary(appDir, "..\\bin", libraryNameWithExtension)) return;
      } else {
        if (tryLoadLibrary(appDir, "../runtime/Contents/Home/lib", libraryNameWithExtension)) return;
        if (tryLoadLibrary(appDir, libraryNameWithExtension)) return;
      }
    }

    try {
      System.loadLibrary(libraryName);
      return;
    } catch (Throwable t) {
      // Ignore the error, fall back to loading from resource (used in development)
    }

    // Stream the library out of the JAR
    final File tmpFile = File.createTempFile(libraryName, libraryExtension);
    try (InputStream soInJarStream = OSXKeychain.class.getResourceAsStream("/" + libraryNameWithExtension);
         OutputStream soInTmpStream = new FileOutputStream(tmpFile)) {
      // Put the library in a temp file.
      File soInTmp = File.createTempFile(libraryName, libraryExtension);
      soInTmp.deleteOnExit();

      // Copy the .so
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = soInJarStream.read(buffer)) > 0) {
        soInTmpStream.write(buffer, 0, bytesRead);
      }
    }

    // Now load the library
    System.load(tmpFile.getAbsolutePath());
  }

  private static boolean tryLoadLibrary(String... pathElements) {
    File file = null;
    for (String pathElement : pathElements) {
      if (file == null)
        file = new File(pathElement);
      else
        file = new File(file, pathElement);
    }
    if (file == null) {
      return false;
    }
    try {
      System.load(file.getAbsolutePath());
      return true;
    } catch (Throwable ignored) {
    }
    return false;
  }

  /* ********************************* */
  /* Private utilities from here down. */
  /* ********************************* */

  /**
   * A fixed mapping of ports to known protocols.
   */
  private static final Map<Integer, pt.davidafsilva.apple.OSXKeychainProtocolType> PROTOCOLS;

  static {
    PROTOCOLS = new HashMap<>(32);
    PROTOCOLS.put(548, pt.davidafsilva.apple.OSXKeychainProtocolType.AFP);
    PROTOCOLS.put(3020, pt.davidafsilva.apple.OSXKeychainProtocolType.CIFS);
    PROTOCOLS.put(2401, pt.davidafsilva.apple.OSXKeychainProtocolType.CVSpserver);
    PROTOCOLS.put(3689, pt.davidafsilva.apple.OSXKeychainProtocolType.DAAP);
    PROTOCOLS.put(3031, pt.davidafsilva.apple.OSXKeychainProtocolType.EPPC);
    PROTOCOLS.put(21, pt.davidafsilva.apple.OSXKeychainProtocolType.FTP);
    PROTOCOLS.put(990, pt.davidafsilva.apple.OSXKeychainProtocolType.FTPS);
    PROTOCOLS.put(80, pt.davidafsilva.apple.OSXKeychainProtocolType.HTTP);
    PROTOCOLS.put(443, pt.davidafsilva.apple.OSXKeychainProtocolType.HTTPS);
    PROTOCOLS.put(143, pt.davidafsilva.apple.OSXKeychainProtocolType.IMAP);
    PROTOCOLS.put(993, pt.davidafsilva.apple.OSXKeychainProtocolType.IMAPS);
    PROTOCOLS.put(631, pt.davidafsilva.apple.OSXKeychainProtocolType.IPP);
    PROTOCOLS.put(6667, pt.davidafsilva.apple.OSXKeychainProtocolType.IRC);
    PROTOCOLS.put(994, pt.davidafsilva.apple.OSXKeychainProtocolType.IRCS);
    PROTOCOLS.put(389, pt.davidafsilva.apple.OSXKeychainProtocolType.LDAP);
    PROTOCOLS.put(636, pt.davidafsilva.apple.OSXKeychainProtocolType.LDAPS);
    PROTOCOLS.put(119, pt.davidafsilva.apple.OSXKeychainProtocolType.NNTP);
    PROTOCOLS.put(563, pt.davidafsilva.apple.OSXKeychainProtocolType.NNTPS);
    PROTOCOLS.put(110, pt.davidafsilva.apple.OSXKeychainProtocolType.POP3);
    PROTOCOLS.put(995, pt.davidafsilva.apple.OSXKeychainProtocolType.POP3S);
    PROTOCOLS.put(554, pt.davidafsilva.apple.OSXKeychainProtocolType.RTSP);
    PROTOCOLS.put(25, pt.davidafsilva.apple.OSXKeychainProtocolType.SMTP);
    PROTOCOLS.put(1080, pt.davidafsilva.apple.OSXKeychainProtocolType.SOCKS);
    PROTOCOLS.put(22, pt.davidafsilva.apple.OSXKeychainProtocolType.SSH);
    PROTOCOLS.put(3690, pt.davidafsilva.apple.OSXKeychainProtocolType.SVN);
    PROTOCOLS.put(23, pt.davidafsilva.apple.OSXKeychainProtocolType.Telnet);
    PROTOCOLS.put(992, pt.davidafsilva.apple.OSXKeychainProtocolType.TelnetS);
  }
}
