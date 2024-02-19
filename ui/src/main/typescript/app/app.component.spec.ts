import { Title } from '@angular/platform-browser';
import { Spectator, createRoutingFactory,
    mockProvider } from '@ngneat/spectator/jest';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
    const createComponent = createRoutingFactory({
        component: AppComponent,
        shallow: true,
        providers: [
            mockProvider(Title)
        ]
    });
    let spectator: Spectator<AppComponent>;

    beforeEach(() => spectator = createComponent());

    it('gets created', () => {
        expect(spectator.component).toBeTruthy();
    });

    it('sets title', () => {
        expect(spectator.inject(Title).setTitle)
            .toHaveBeenCalledWith('Segovia');
    });

    it('renders headline', () => {
        expect(spectator.query('h1')).toHaveText('Segovia');
    });
});
