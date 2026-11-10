import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LoteCadastro } from '../models/lote-cadastro.model';
import { LoteResponse } from '../models/lote-response.model';

@Injectable({
  providedIn: 'root'
})
export class LoteRetalhoService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/lotes';

  // --- Função Original ---

  /**
   * Cadastra um novo lote.
   */
  cadastrar(dados: LoteCadastro): Observable<LoteResponse> {
    return this.http.post<LoteResponse>(
      this.apiUrl,
      dados
    );
  }

  // --- Funções Integradas para a Dashboard ---

  /**
   * Retorna todos os lotes para exibição em tabelas ou listagens da dashboard.
   */
  listarTodos(): Observable<LoteResponse[]> {
    return this.http.get<LoteResponse[]>(this.apiUrl);
  }

  /**
   * Retorna os detalhes de um lote específico pelo ID para visualização na dashboard.
   */
  buscarPorId(id: number | string): Observable<LoteResponse> {
    return this.http.get<LoteResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Atualiza as informações de um lote existente a partir da dashboard.
   */
  atualizar(id: number | string, dados: Partial<LoteCadastro>): Observable<LoteResponse> {
    return this.http.put<LoteResponse>(`${this.apiUrl}/${id}`, dados);
  }

  /**
   * Remove um lote através da dashboard.
   */
  deletar(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
