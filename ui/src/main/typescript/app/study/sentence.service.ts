import { Injectable } from '@angular/core';
import { EMPTY, Observable } from 'rxjs';
import { Sentence } from './sentence';

@Injectable({ providedIn: 'root' })
export class SentenceService {
    sentences: Observable<Sentence[]> = EMPTY;
}
