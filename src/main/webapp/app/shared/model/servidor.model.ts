import { IUso } from 'app/shared/model//uso.model';
import { IJustificativa } from 'app/shared/model//justificativa.model';
import { IReserva } from 'app/shared/model//reserva.model';
import { IOrgaoPublico } from 'app/shared/model//orgao-publico.model';

export interface IServidor {
    id?: number;
    nome?: string;
    numMatricula?: string;
    cargo?: string;
    uso?: IUso;
    justificativa?: IJustificativa;
    reservas?: IReserva[];
    orgaoPublico?: IOrgaoPublico;
}

export class Servidor implements IServidor {
    constructor(
        public id?: number,
        public nome?: string,
        public numMatricula?: string,
        public cargo?: string,
        public uso?: IUso,
        public justificativa?: IJustificativa,
        public reservas?: IReserva[],
        public orgaoPublico?: IOrgaoPublico
    ) {}
}
