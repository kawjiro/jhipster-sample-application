import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEquipamento } from 'app/shared/model/equipamento.model';

type EntityResponseType = HttpResponse<IEquipamento>;
type EntityArrayResponseType = HttpResponse<IEquipamento[]>;

@Injectable({ providedIn: 'root' })
export class EquipamentoService {
    public resourceUrl = SERVER_API_URL + 'api/equipamentos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/equipamentos';

    constructor(private http: HttpClient) {}

    create(equipamento: IEquipamento): Observable<EntityResponseType> {
        return this.http.post<IEquipamento>(this.resourceUrl, equipamento, { observe: 'response' });
    }

    update(equipamento: IEquipamento): Observable<EntityResponseType> {
        return this.http.put<IEquipamento>(this.resourceUrl, equipamento, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEquipamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEquipamento[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEquipamento[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
