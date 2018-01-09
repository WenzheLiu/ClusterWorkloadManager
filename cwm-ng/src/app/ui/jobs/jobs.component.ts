import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {

  host: string;
  port: string;

  constructor(route: ActivatedRoute) {
    route.params.subscribe(params => {
      this.host = params['host'];
      this.port = params['port'];
    });
  }

  ngOnInit() {
  }

}
