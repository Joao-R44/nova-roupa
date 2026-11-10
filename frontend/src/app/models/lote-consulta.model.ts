import { TipoTecido } from './tipo-tecido.model';
import { StatusLote } from './status-lote.model';

export interface LoteConsulta {
  id: number;
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