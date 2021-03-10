import { Moment } from 'moment';

export interface IVacinacaoPessoa {
  id?: number;
  quando?: Moment;
  cns?: string;
  codigoProfissional?: string;
  pessoaNome?: string;
  pessoaId?: number;
  vacinaNome?: string;
  vacinaId?: number;
  fabricanteNome?: string;
  fabricanteId?: number;
}

export class VacinacaoPessoa implements IVacinacaoPessoa {
  constructor(
    public id?: number,
    public quando?: Moment,
    public cns?: string,
    public codigoProfissional?: string,
    public pessoaNome?: string,
    public pessoaId?: number,
    public vacinaNome?: string,
    public vacinaId?: number,
    public fabricanteNome?: string,
    public fabricanteId?: number
  ) {}
}
