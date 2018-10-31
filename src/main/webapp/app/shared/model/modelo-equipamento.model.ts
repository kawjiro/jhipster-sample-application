import { IEquipamento } from 'app/shared/model//equipamento.model';

export interface IModeloEquipamento {
    id?: number;
    nomeModelo?: string;
    numPatrimonio?: number;
    equipamento?: IEquipamento;
}

export class ModeloEquipamento implements IModeloEquipamento {
    constructor(public id?: number, public nomeModelo?: string, public numPatrimonio?: number, public equipamento?: IEquipamento) {}
}
