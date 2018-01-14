import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CwmService } from '../../service/cwm.service';
import { Observable } from 'rxjs/Observable';
import { Worker } from '../../model/worker';

@Component({
  selector: 'app-workers',
  templateUrl: './workers.component.html',
  styleUrls: ['./workers.component.css']
})
export class WorkersComponent implements OnInit {

  host: string;
  port: number;
  workers$: Observable<Worker[]>;

  constructor(private route: ActivatedRoute, private cwmService: CwmService) {
    route.params.subscribe(params => {
      this.host = params['host'];
      this.port = params['port'];
      this.workers$ = cwmService.workers(this.host, this.port);
    });
  }

  ngOnInit() {
  }

}
