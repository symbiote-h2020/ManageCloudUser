package hr.fer.tel.symbiote;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import eu.h2020.symbiote.security.commons.enums.AccountStatus;
import eu.h2020.symbiote.security.commons.enums.OperationType;
import eu.h2020.symbiote.security.commons.enums.UserRole;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.communication.AAMClient;
import eu.h2020.symbiote.security.communication.IAAMClient;
import eu.h2020.symbiote.security.communication.payloads.AAM;
import eu.h2020.symbiote.security.communication.payloads.Credentials;
import eu.h2020.symbiote.security.communication.payloads.UserDetails;
import eu.h2020.symbiote.security.communication.payloads.UserManagementRequest;

public class RegisterUser extends UsersBase {

  public RegisterUser() throws SecurityHandlerException {
    super();
  }

  public static void main(String[] args) throws SecurityHandlerException {

    RegisterUser registerUser = new RegisterUser();
    logInfo("************** friend");
    registerUser.registerToPAAM("friend", "pass", "friend@fer.hr", Map.of());
    logInfo("************** parent");
    registerUser.registerToPAAM("parent", "pass", "parent@fer.hr", Map.of("enterRoom_C8-18", "yes", "enterRoom_C8-16", "yes"));
    logInfo("************** majstorSearch");
    registerUser.registerToPAAM("repairmanSearch", "pass", "repairman@fer.hr", Map.of("enterRoom_C8-16", "yes"));
  }

  public void registerToPAAM(String userUsername, String userPassword, String email, Map<String, String> attributes) {
    try {

      Optional<String> opAAMUrl = Optional.ofNullable(directAAMUrl);
      IAAMClient aamClient;

      if (opAAMUrl.isPresent()) {
        logInfo("Registering to PAAM: " + platformId + " with url " + opAAMUrl.get());
        aamClient = new AAMClient(opAAMUrl.get());
      } else {
        Map<String, AAM> availableAAMs = securityHandler.getAvailableAAMs();
        aamClient = new AAMClient(availableAAMs.get(platformId).getAamAddress());
        logInfo("Registering to PAAM: " + platformId + " with url " + availableAAMs.get(platformId).getAamAddress());
      }

      UserManagementRequest userManagementRequest = new UserManagementRequest(
          new Credentials(paamOwnerUsername, paamOwnerPassword),
          new Credentials(userUsername, userPassword),
          new UserDetails(
              new Credentials(userUsername, userPassword), // userCredentials
              email, // recoveryMail
              UserRole.USER, // UserRole
              AccountStatus.ACTIVE, // AccountStatus
              attributes, // Map<String, String> attributes
              new HashMap<>(), // Map<String, Certificate> clients
              true, // serviceConsent
              true // analyticsAndResearchConsent
              ),
          OperationType.CREATE);

      try {
        aamClient.manageUser(userManagementRequest);
        logInfo("User registration done");
      } catch (AAMException e) {
        e.printStackTrace();
      }
    } catch (SecurityHandlerException e) {
      e.printStackTrace();
    }
  }
}
