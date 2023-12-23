import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { StudyComponent } from './study/study.component';

export const routes: Routes = [
    {
        path: 'home',
        component: HomeComponent
    },
    {
        path: 'study',
        component: StudyComponent
    },
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    }
];
