import { IModeloEquipamento } from 'app/shared/model//modelo-equipamento.model';
import { IManutencao } from 'app/shared/model//manutencao.model';
import { IReserva } from 'app/shared/model//reserva.model';

export const enum StatusEquipamento {
    Funcionando = 'Funcionando',
    Defeito = 'Defeito'
}

export interface IEquipamento {
    id?: number;
    descricao?: string;
    numPatrimonio?: number;
    status?: StatusEquipamento;
    modeloEquipamentos?: IModeloEquipamento[];
    manutencaos?: IManutencao[];
    reserva?: IReserva;
}

export class Equipamento implements IEquipamento {
    constructor(
        public id?: number,
        public descricao?: string,
        public numPatrimonio?: number,
        public status?: StatusEquipamento,
        public modeloEquipamentos?: IModeloEquipamento[],
        public manutencaos?: IManutencao[],
        public reserva?: IReserva
    ) {}
}
