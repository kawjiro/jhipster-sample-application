import { Moment } from 'moment';
import { IServidor } from 'app/shared/model//servidor.model';

export interface IUso {
    id?: number;
    dataInicio?: Moment;
    dataFim?: Moment;
    tipoEquipamento?: string;
    servidor?: IServidor;
}

export class Uso implements IUso {
    constructor(
        public id?: number,
        public dataInicio?: Moment,
        public dataFim?: Moment,
        public tipoEquipamento?: string,
        public servidor?: IServidor
    ) {}
}
