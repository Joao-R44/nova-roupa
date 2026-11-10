import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { environment } from '../../environments/environment';
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);

  private apiUrl = `${environment.apiUrl}/auth`;

  // --- Funções Originais de Autenticação via HTTP ---

  login(dados: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      dados
    ).pipe(
      tap(resposta => {
        sessionStorage.setItem('token', resposta.token);
        
        // Opcional: Se a resposta da API trouxer o nome do usuário, você pode salvá-lo para a dashboard
        // if (resposta.usuario) {
        //   this.setUsuarioLogado(resposta.usuario);
        // }
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem('token');
    // Limpa também os dados da dashboard ao deslogar
    this.removerUsuarioLogado();
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  // --- Funções Integradas para a Dashboard ---

  /**
   * Salva o nome ou dados do usuário logado para exibição na dashboard.
   */
  setUsuarioLogado(usuario: string): void {
    sessionStorage.setItem('usuarioLogado', usuario);
  }

  /**
   * Retorna o usuário logado atualmente (útil para personalizar a dashboard).
   */
  getUsuarioLogado(): string | null {
    return sessionStorage.getItem('usuarioLogado');
  }

  /**
   * Remove apenas os dados do usuário da sessão da dashboard.
   */
  removerUsuarioLogado(): void {
    sessionStorage.removeItem('usuarioLogado');
  }

  /**
   * Verifica se o usuário está autenticado com base na existência do Token.
   */
  estaAutenticado(): boolean {
    return this.getToken() !== null;
  }
}
