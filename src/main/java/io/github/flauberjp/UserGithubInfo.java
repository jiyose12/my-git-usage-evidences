package io.github.flauberjp;

import static io.github.flauberjp.util.MyLogger.LOGGER;

import io.github.flauberjp.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import lombok.Getter;
import lombok.ToString;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

@Getter
@ToString
public class UserGithubInfo implements Serializable {

  public static final String MY_GIT_USAGE_EVIDENCES_REPO = "my-git-usage-evidences-repo";
  public static final String PROPERTIES_FILE = "propriedades.txt";
  private static UserGithubInfo userGithubInfo;
  // default serialVersion id
  private static final long serialVersionUID = 1L;
  private String repoName = MY_GIT_USAGE_EVIDENCES_REPO;
  private String username = ""; // e.g. flauberjp
  private String password = ""; // e.g. passw0rd
  private String githubName = ""; // e.g. Flaviano Flauber
  private String githubEmail = ""; // e.g. flauberjp@gmail.com
  private GitHub gitHub = null;
  private GHUser ghUser = null;
  private String hookType = ""; // e.g. pre-commit
  private boolean credenciaisValidas = false;

  private UserGithubInfo() {
  }

  private UserGithubInfo(Properties properties) {
    LOGGER.debug("UserGithubInfo.UserGithubInfo(properties = {}", Util.camuflaPasswordDeUmProperties(properties));
    username = properties.getProperty("login");
    password = properties.getProperty("password");
    repoName = properties.getProperty("repoName");
    hookType = properties.getProperty("hookType");

    githubName = properties.getProperty("githubName");
    githubEmail = properties.getProperty("githubEmail");
  }

  private UserGithubInfo(String username, String password) {
    LOGGER.debug("UserGithubInfo.UserGithubInfo(username {}, password XXX)", username);
    try {
      this.gitHub = GitHub.connectUsingPassword(username, password);
      this.ghUser = gitHub.getUser(username);
      this.username = username;
      this.password = password;
      this.credenciaisValidas = true;
      this.githubName = ghUser.getName();
      this.githubEmail = ghUser.getEmail();
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
      this.credenciaisValidas = false;
    }
  }

  private UserGithubInfo(String username, String password, String hookType) {
    try {
      this.gitHub = GitHub.connectUsingPassword(username, password);
      this.ghUser = gitHub.getUser(username);
      this.username = username;
      this.password = password;
      this.credenciaisValidas = true;
      this.githubName = ghUser.getName();
      this.githubEmail = ghUser.getEmail();
      this.hookType = hookType;
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
      this.credenciaisValidas = false;
    }
  }

  public static void setRepoName(String repoName) {
    LOGGER.debug("UserGithubInfo.setRepoName(repoName = {})", repoName);
    userGithubInfo.repoName = repoName;
  }

  public static UserGithubInfo get() throws IOException {
    LOGGER.debug("UserGithubInfo.get()");
    if (userGithubInfo == null) {
      return get(Util.getProperties(PROPERTIES_FILE));
    }
    return userGithubInfo;
  }

  public static UserGithubInfo get(Properties properties) throws IOException {
    LOGGER.debug("UserGithubInfo.get(properties = {})", Util.camuflaPasswordDeUmProperties(properties));
    if (userGithubInfo == null) {
      userGithubInfo = new UserGithubInfo(properties);
    }
    return userGithubInfo;
  }

  public static UserGithubInfo get(String username, String password) {
    LOGGER.debug("UserGithubInfo.get(username {}, password XXX)", username);
    if (userGithubInfo == null) {
      userGithubInfo = new UserGithubInfo(username, password);
    }
    return userGithubInfo;
  }

  public static UserGithubInfo get(String username, String password, String hookType) {
    if (userGithubInfo == null) {
      userGithubInfo = new UserGithubInfo(username, password, hookType);
    }
    return userGithubInfo;
  }

  public static void reset() {
    LOGGER.debug("UserGithubInfo.reset()");
    userGithubInfo = null;
  }

  public Properties toProperties() {
    LOGGER.debug("UserGithubInfo.toProperties()");
    return Util.createProperties(new String[]{
        "repoName", getRepoName(),
        "login", getUsername(),
        "password", getPassword(),
        "githubName", getGithubName(),
        "githubEmail", getGithubEmail(),
        "hookType", getHookType()
    });
  }

  ;

  public String getGithub() {
    LOGGER.debug("UserGithubInfo.getGithub()");
    return "https://github.com/" + getUsername() + "/";
  }

  public String getRepoNameFullPath() {
    LOGGER.debug("UserGithubInfo.getRepoNameFullPath()");
    return getGithub() + getRepoName() + ".git";
  }

  public boolean isCredenciaisValidas() {
    LOGGER.debug("UserGithubInfo.isCredenciaisValidas()");
    return credenciaisValidas;
  }

  public static boolean validarCredenciais(String username, String password) {
    LOGGER.debug("UserGithubInfo.validarCredenciais(username {}, password XXX)", username);
    UserGithubInfo user = UserGithubInfo.get(username, password);
    return user.isCredenciaisValidas();
  }

  public String toString() {
    String var10000 = this.getRepoName();
    return "UserGithubInfo("
        + "repoName=" + var10000
        + ", username=" + this.getUsername()
        + ", password=XXX"
        + ", githubName=" + this.getGithubName()
        + ", githubEmail=" + this.getGithubEmail()
        + ", gitHub=" + this.getGithub()
        + ", ghUser=" + this.getGhUser()
        + ", hookType="
        + this.getHookType()
        + ", credenciaisValidas=" + this.isCredenciaisValidas()
        + ")";
  }
  
  public static void resetRepoName() {
    LOGGER.debug("UserGithubInfo.resetRepoName()");
	  setRepoName(MY_GIT_USAGE_EVIDENCES_REPO);
  }
}
