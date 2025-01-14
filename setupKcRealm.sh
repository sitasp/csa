#!/bin/sh

realmName="csa-realm"
client_id="csa-client"
client_secret="Ja2ANiahugUGag4Pw3SLdEe2PDELoG5P"

echo "Starting creation of $realmName realm. Your clientId: $client_id clientSecret: $client_secret"

# login
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin --password admin

# Create realm
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create realms -s realm=$realmName -s enabled=true

# Create 'MESSAGES:READ' scope and client
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create -x "client-scopes" -r $realmName -s name=MESSAGES:READ -s protocol=openid-connect
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create clients -r $realmName -s clientId=$client_id -s enabled=true -s clientAuthenticatorType=client-secret -s secret=$client_secret  -s 'redirectUris=["*"]' -s 'defaultClientScopes=["MESSAGES:READ", "web-origins", "profile", "roles", "email"]'

# create realm roles
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create roles -r $realmName -s name=ROLE_USER -s 'description=Regular user with limited set of permissions'
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create roles -r $realmName -s name=ROLE_ADMIN -s 'description=Admin user unlimited access'

# Create demoUser, assign password, role and scope; name should be in lowercase
testUser="testsatish"
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create users -r $realmName -s username=$testUser -s enabled=true
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh set-password -r $realmName --username $testUser --new-password $testUser
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh add-roles --uusername $testUser --rolename ROLE_USER -r $realmName

# Create demoAdmin, assign password, role and scope; name should be in lowercase
testAdmin="testadmin"
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh create users -r $realmName -s username=$testAdmin -s enabled=true
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh set-password -r $realmName --username $testAdmin --new-password $testAdmin
docker-compose exec keycloak /opt/keycloak/bin/kcadm.sh add-roles --uusername $testAdmin --rolename ROLE_ADMIN --rolename ROLE_USER -r $realmName
