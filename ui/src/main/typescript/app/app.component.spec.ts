import { Spectator, createComponentFactory } from '@ngneat/spectator/jest';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
    const createComponent = createComponentFactory({
        component: AppComponent,
        shallow: true
    });
    let spectator: Spectator<AppComponent>;

    beforeEach(() => spectator = createComponent());

    it('creates the app', () => {
        expect(spectator.component).toBeTruthy();
    });

    it('sets title', () => {
        expect(spectator.component.title).toBe('Segovia');
    });

    it('renders headline', () => {
        expect(spectator.query('h1')).toHaveText('Segovia');
    });
});
