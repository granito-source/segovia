import { Injectable, OnDestroy } from '@angular/core';
import { Collection, completeWith, follow,
    readCollection } from '@granito/ngx-hal-client';
import { catchError, EMPTY, filter, map, Observable,
    ReplaySubject } from 'rxjs';
import { ApiService } from '../api/api.service';
import { Sentence } from './sentence';

@Injectable({ providedIn: 'root' })
export class SentenceService implements OnDestroy {
    private sentences$ = new ReplaySubject<Collection<Sentence>>(1);

    get sentences(): Observable<Sentence[]> {
        return this.sentences$.pipe(
            map(collection => collection.values)
        );
    }

    constructor(apiService: ApiService) {
        apiService.root.pipe(
            completeWith(this.sentences$),
            follow('sentences'),
            filter(accessor => accessor.canRead),
            readCollection(Sentence),
            catchError(this.handleError)
        ).subscribe(collection => this.sentences$.next(collection));
    }

    ngOnDestroy(): void {
        this.sentences$.complete();
    }

    private handleError(error: Error): Observable<never> {
        console.log(`error reading sentences: ${error.message}`);

        return EMPTY;
    }
}
