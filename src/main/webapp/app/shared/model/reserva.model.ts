import { Moment } from 'moment';
import { IEquipamento } from 'app/shared/model//equipamento.model';
import { IServidor } from 'app/shared/model//servidor.model';

export interface IReserva {
    id?: number;
    dataHoraReserva?: Moment;
    dataHoraRetirdaPrev?: Moment;
    dataHoraDevolucaoPrev?: Moment;
    equipamentos?: IEquipamento[];
    servidor?: IServidor;
}

export class Reserva implements IReserva {
    constructor(
        public id?: number,
        public dataHoraReserva?: Moment,
        public dataHoraRetirdaPrev?: Moment,
        public dataHoraDevolucaoPrev?: Moment,
        public equipamentos?: IEquipamento[],
        public servidor?: IServidor
    ) {}
}
