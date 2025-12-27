import { createServiceFactory, createSpyObject, SpectatorService,
    SpyObject } from '@ngneat/spectator/vitest';
import { ApiService } from './api.service';
import { Accessor, HAL_ROOT } from '@granito/ngx-hal-client';
import { ApiRoot } from './api-root';
import { cold } from '@granito/vitest-marbles';

describe('ApiService', () => {
    const createService = createServiceFactory({
        service: ApiService
    });
    const apiRoot = new ApiRoot({
        _links: {
            self: { href: '/api/v1' }
        }
    });
    let halRoot: SpyObject<Accessor>;
    let spectator: SpectatorService<ApiService>;

    beforeEach(() => {
        halRoot = createSpyObject(Accessor);
        halRoot.read.andReturn(cold('--r|', { r: apiRoot }));
        spectator = createService({
            providers: [
                {
                    provide: HAL_ROOT,
                    useValue: halRoot
                }
            ]
        });
    });

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });

    it('reads and presents API root', () => {
        expect(spectator.service.root)
            .toBeObservable(cold('--r', { r: apiRoot }));
        expect(cold('')).toSatisfyOnFlush(() => {
            expect(halRoot.read).toHaveBeenCalledOnce();
            expect(halRoot.read).toHaveBeenCalledWith(ApiRoot);
        });
    });
});
