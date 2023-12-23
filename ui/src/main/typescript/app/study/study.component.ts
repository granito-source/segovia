import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
    selector: 'study',
    standalone: true,
    templateUrl: './study.component.html',
    styleUrl: './study.component.css'
})
export class StudyComponent implements OnInit {
    constructor(private title: Title) {
    }

    ngOnInit(): void {
        this.title.setTitle('Segovia: Study');
    }
}
