import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = sessionStorage.getItem('token');

  const isApiRequest = req.url.startsWith('http://localhost:8080/');

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