package hr.fer.tel.symbiote;

import eu.h2020.symbiote.security.ClientSecurityHandlerFactory;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.handler.ISecurityHandler;

public class UsersBase {

  protected ISecurityHandler securityHandler;
  protected String paamOwnerUsername = "paamOwnerUsername";
  protected String paamOwnerPassword = "paamOwnerPassword";
  protected String coreAAMAddress = "https://symbiote-open.man.poznan.pl/coreInterface";
  protected String keystorePath = "testKeystore";
  protected String keystorePassword = "keystorePass";
  protected String platformId = "DemoPlatform2";
  protected String directAAMUrl = "http://localhost:8080";

  public UsersBase() throws SecurityHandlerException {

    securityHandler = ClientSecurityHandlerFactory.getSecurityHandler(coreAAMAddress, keystorePath,
        keystorePassword);
  }

  public static void logInfo(String message) {
    System.out.println(message);
  }
}
