import { Accessor, Collection, HalError } from '@granito/ngx-hal-client';
import { SpectatorHttp, createHttpFactory, createSpyObject, mockProvider } from '@ngneat/spectator/jest';
import { cold } from 'jest-marbles';
import { Subject } from 'rxjs';
import { ApiRoot } from '../api/api-root';
import { ApiRootService } from '../api/api-root.service';
import { Sentence } from './sentence';
import { SentenceService } from './sentence.service';

describe('SentenceService', () => {
    const createService = createHttpFactory({
        service: SentenceService
    });

    let apiRoot: Subject<ApiRoot>;
    let spectator: SpectatorHttp<SentenceService>;

    beforeEach(() => {
        apiRoot = new Subject();
        spectator = createService({
            providers: [
                mockProvider(ApiRootService, { apiRoot })
            ]
        });
    });

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });

    it('subscribes to API Root for the lifetime', () => {
        expect(apiRoot.observed).toBe(true);

        spectator.service.ngOnDestroy();

        expect(apiRoot.observed).toBe(false);
    });

    describe('#sentences', () => {
        it('presents nothing initially', () => {
            expect(spectator.service.sentences).toBeObservable(cold(''));
        });

        it('presents nothing when API root has no link', () => {
            const api = createSpyObject(ApiRoot);
            const timer = cold('--t');

            api.follow.andReturn(undefined);

            timer.subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold(''));
            expect(timer).toSatisfyOnFlush(() => {
                expect(api.follow).toHaveBeenCalledTimes(1);
                expect(api.follow)
                    .toHaveBeenCalledWith('sentences', undefined);
            });
        });

        it('presents API results when API root has link', () => {
            const api = createSpyObject(ApiRoot);
            const accessor = createSpyObject(Accessor);
            const timer = cold('--t');

            api.follow.andReturn(accessor);
            accessor.readCollection.andReturn(cold('-s|', {
                s: new Collection(Sentence, {
                    _embedded: {
                        sentences: [
                            { id: 'one', text: 'One.' },
                            { id: 'two', text: 'Two.' }
                        ]
                    }
                })
            }));

            timer.subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold('---s', {
                s: [
                    new Sentence({ id: 'one', text: 'One.' }),
                    new Sentence({ id: 'two', text: 'Two.' })
                ]
            }));
            expect(timer).toSatisfyOnFlush(() => {
                expect(api.follow).toHaveBeenCalledTimes(1);
                expect(api.follow)
                    .toHaveBeenCalledWith('sentences', undefined);
                expect(accessor.readCollection).toHaveBeenCalledTimes(1);
                expect(accessor.readCollection)
                    .toHaveBeenCalledWith(Sentence);
            });
        });

        it('presents nothing when API call fails', () => {
            const api = createSpyObject(ApiRoot);
            const accessor = createSpyObject(Accessor);
            const timer = cold('--t');

            api.follow.andReturn(accessor);
            accessor.readCollection.andReturn(cold('--#', undefined,
                new HalError({ message: 'read collection' })));

            timer.subscribe(() => apiRoot.next(api));

            expect(spectator.service.sentences).toBeObservable(cold(''));
        });
    });
});
