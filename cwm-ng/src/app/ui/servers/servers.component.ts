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

  servers: Server[] = [];
  selected: boolean[] = [];
  jobCommand = '';

  constructor(private cwmService: CwmService) {
    cwmService.servers().subscribe(servers => {
      this.servers = servers;
      this.selected = servers.map(s => false);
    });
  }

  ngOnInit() {
  }

  allSelected(): boolean {
    if (this.selected) {
      return this.selected.every(sel => sel === true);
    } else {
      return false;
    }
  }

  onSelectServer(index: number, event) {
    this.selected[index] = event.target.checked;
  }

  onSelectAllServers(event) {
    this.selected.fill(event.target.checked);
  }

  onRunJob() {
    const job = this.jobCommand.split(' ').filter(str => str.length > 0);
    if (job.length === 0) {
      return;
    }
    this.cwmService.runJob(job, this.selectedServers());
  }

  onShutdown() {
    this.cwmService.shutdown(this.selectedServers());
  }

  private selectedServers(): Server[] {
    const selectedServs: Server[] = [];
    for (let i = 0; i < this.servers.length; i++) {
      if (this.selected[i]) {
        console.log(this.servers[i]);
        selectedServs.push(this.servers[i]);
      }
    }
    return selectedServs;
  }
}
