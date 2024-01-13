import { SpectatorService, createServiceFactory } from '@ngneat/spectator/jest';
import { ApiRootService } from './api-root.service';

describe('ApiRootService', () => {
    const createService = createServiceFactory({
        service: ApiRootService
    });
    let spectator: SpectatorService<ApiRootService>;

    beforeEach(() => spectator = createService());

    it('gets created', () => {
        expect(spectator.service).toBeTruthy();
    });
});
