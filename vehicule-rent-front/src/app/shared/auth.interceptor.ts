import { HttpInterceptorFn } from '@angular/common/http';
import {getKeycloak} from "./keycloak-init";


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const kc = getKeycloak();

  if (kc?.token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${kc.token}`
      }
    });
  }

  return next(req);
};
