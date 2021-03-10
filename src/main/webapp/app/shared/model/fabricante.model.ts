import { Moment } from 'moment';
import { IVacina } from 'app/shared/model/vacina.model';

export interface IFabricante {
  id?: number;
  nome?: string;
  criado?: Moment;
  paisNome?: string;
  paisId?: number;
  vacinas?: IVacina[];
}

export class Fabricante implements IFabricante {
  constructor(
    public id?: number,
    public nome?: string,
    public criado?: Moment,
    public paisNome?: string,
    public paisId?: number,
    public vacinas?: IVacina[]
  ) {}
}
