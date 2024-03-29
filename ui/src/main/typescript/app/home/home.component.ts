import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
    selector: 'home',
    standalone: true,
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
    constructor(private title: Title) {
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia: Home');
    }
}
