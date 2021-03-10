import { Moment } from 'moment';
import { IFabricante } from 'app/shared/model/fabricante.model';

export interface IVacina {
  id?: number;
  nome?: string;
  criada?: Moment;
  doencaNome?: string;
  doencaId?: number;
  fabricantes?: IFabricante[];
}

export class Vacina implements IVacina {
  constructor(
    public id?: number,
    public nome?: string,
    public criada?: Moment,
    public doencaNome?: string,
    public doencaId?: number,
    public fabricantes?: IFabricante[]
  ) {}
}
