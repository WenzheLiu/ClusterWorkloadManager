import { Component, OnInit } from '@angular/core';
import { CwmService } from '../service/cwm.service';
import { Observable } from 'rxjs/Observable';
import { Server } from '../model/server';

@Component({
  selector: 'app-servers',
  templateUrl: './servers.component.html',
  styleUrls: ['./servers.component.css']
})
export class ServersComponent implements OnInit {

  servers$: Observable<Server[]>;

  constructor(private cwmService: CwmService) {
    this.servers$ = cwmService.servers();
    this.servers$.subscribe(servers => console.log(servers));
  }

  ngOnInit() {
  }

}
