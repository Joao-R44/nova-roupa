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

  cadastrar(dados: LoteCadastro): Observable<LoteResponse> {
    return this.http.post<LoteResponse>(
      this.apiUrl,
      dados
    );
  }
}