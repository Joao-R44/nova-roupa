import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { LoteCadastro } from '../models/lote-cadastro.model';
import { LoteResponse } from '../models/lote-response.model';
import { LoteConsulta } from '../models/lote-consulta.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class LoteRetalhoService {

  private http = inject(HttpClient);

  private apiUrl = `${environment.apiUrl}/lotes`;

  listar(page = 0, size = 10): Observable<Page<LoteConsulta>> {
    return this.http.get<Page<LoteConsulta>>(this.apiUrl, {
      params: { page, size }
    });
  }

  // --- Função Original ---

  /**
   * Cadastra um novo lote.
   */
  cadastrar(dados: LoteCadastro): Observable<LoteResponse> {
    return this.http.post<LoteResponse>(this.apiUrl, dados);
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
