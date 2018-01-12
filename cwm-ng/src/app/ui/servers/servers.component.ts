import { Component, OnInit } from '@angular/core';
import { CwmService } from '../../service/cwm.service';
import { Observable } from 'rxjs/Observable';
import { Server } from '../../model/server';

@Component({
  selector: 'app-servers',
  templateUrl: './servers.component.html',
  styleUrls: ['./servers.component.css']
})
export class ServersComponent implements OnInit {

  servers$: Observable<Server[]>;
  selected: Boolean[];

  constructor(private cwmService: CwmService) {
    this.servers$ = cwmService.servers();
    this.servers$.subscribe(servers => this.selected = new Boolean[servers.length]);
  }

  ngOnInit() {
  }

  onClickCheckBox(index: number, value: boolean) {
    this.selected[index] = value;
    console.log(this.selected);
  }
}
