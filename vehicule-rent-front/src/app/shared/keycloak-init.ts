import Keycloak from 'keycloak-js';

let keycloak: Keycloak;

export function initializeKeycloak() {
  return () =>
    new Promise<void>((resolve, reject) => {
      keycloak = new Keycloak({
        url: 'http://localhost:8080',
        realm: 'vehicule-app',
        clientId: 'vehicule-rent'
      });

      keycloak
        .init({
          onLoad: 'login-required', // force login before app loads
          pkceMethod: 'S256',
          checkLoginIframe: false
        })
        .then(authenticated => {
          if (!authenticated) {
            keycloak.login();
          } else {
            resolve();
          }
        })
        .catch(err => reject(err));
    });
}


export function getKeycloak() {
  return keycloak;
}
