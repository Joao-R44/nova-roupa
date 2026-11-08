import { TipoTecido } from './tipo-tecido.model';

export interface LoteCadastro {
  tipoTecido: TipoTecido;
  pesoKg: number;
  metragemAproximada?: number;
  corPredominante: string;
  descricao?: string;
  fotosUrls?: string;
  localizacao: string;
}