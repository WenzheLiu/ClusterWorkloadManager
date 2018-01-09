import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-workers',
  templateUrl: './workers.component.html',
  styleUrls: ['./workers.component.css']
})
export class WorkersComponent implements OnInit {

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
