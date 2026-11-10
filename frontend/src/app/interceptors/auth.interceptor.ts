import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = sessionStorage.getItem('token');

  const isApiRequest = req.url.startsWith(`${environment.apiUrl}/`);

  if (token && isApiRequest) {
    const requestWithToken = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(requestWithToken);
  }

  return next(req);
};