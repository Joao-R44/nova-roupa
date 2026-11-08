import { TipoTecido } from './tipo-tecido.model';
import { StatusLote } from './status-lote.model';

export interface LoteResponse {
  id: number;
  vendedor: string;
  tipoTecido: TipoTecido;
  pesoKg: number;
  metragemAproximada: number | null;
  corPredominante: string;
  descricao: string | null;
  fotosUrls: string | null;
  localizacao: string;
  status: StatusLote;
  dataPublicacao: string;
}