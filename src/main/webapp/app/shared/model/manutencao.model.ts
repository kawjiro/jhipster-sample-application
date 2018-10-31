import { IEquipamento } from 'app/shared/model//equipamento.model';

export interface IManutencao {
    id?: number;
    estadoEquipamento?: string;
    equipamento?: IEquipamento;
}

export class Manutencao implements IManutencao {
    constructor(public id?: number, public estadoEquipamento?: string, public equipamento?: IEquipamento) {}
}
