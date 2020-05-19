package hr.fer.tel.symbiote;

import java.util.Map;
import java.util.Optional;

import eu.h2020.symbiote.security.commons.enums.OperationType;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.communication.AAMClient;
import eu.h2020.symbiote.security.communication.IAAMClient;
import eu.h2020.symbiote.security.communication.payloads.AAM;
import eu.h2020.symbiote.security.communication.payloads.Credentials;
import eu.h2020.symbiote.security.communication.payloads.UserDetails;
import eu.h2020.symbiote.security.communication.payloads.UserManagementRequest;

public class UpdateUsersAttribute extends UsersBase {

  public UpdateUsersAttribute() throws SecurityHandlerException {
    super();
  }

  public static void main(String[] args) throws SecurityHandlerException {

    UpdateUsersAttribute registerUser = new UpdateUsersAttribute();
    registerUser.registerToPAAM("repairmanSearch", Map.of("enterRoom_C8-18", "yes"));
  }

  public void registerToPAAM(String userUsername, Map<String, String> attributes) {
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
          new Credentials(userUsername, null),
          new UserDetails(
              new Credentials(userUsername, null), // userCredentials
              null,// email, // recoveryMail
              null,// UserRole.USER, // UserRole
              null,// AccountStatus.ACTIVE, // AccountStatus
              attributes, // Map<String, String> attributes
              null,// new HashMap<>(), // Map<String, Certificate> clients
              true, // serviceConsent
              true // analyticsAndResearchConsent
              ),
          OperationType.ATTRIBUTES_UPDATE);

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
