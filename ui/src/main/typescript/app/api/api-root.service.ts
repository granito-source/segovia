import { Injectable } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { ApiRoot } from './api-root';

@Injectable({ providedIn: 'root' })
export class ApiRootService {
    apiRoot: Observable<ApiRoot> = EMPTY;
}
