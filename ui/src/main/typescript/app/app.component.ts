
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-root',
    imports: [
    RouterOutlet,
    RouterLink
],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
    constructor(private title: Title) {
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia');
    }
}
