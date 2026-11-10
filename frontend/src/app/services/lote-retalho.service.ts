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

  cadastrar(dados: LoteCadastro): Observable<LoteResponse> {
    return this.http.post<LoteResponse>(this.apiUrl, dados);
  }
}