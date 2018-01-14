import { Component, OnInit } from '@angular/core';
import { CwmService } from '../../service/cwm.service';
import { Observable } from 'rxjs/Observable';
import { Server } from '../../model/server';
import { Worker } from '../../model/worker';
import { Job } from '../../model/job';
import { JobDetail } from '../../model/job.detail';

@Component({
  selector: 'app-servers',
  templateUrl: './servers.component.html',
  styleUrls: ['./servers.component.css']
})
export class ServersComponent implements OnInit {

  servers: Server[] = [];
  selected: boolean[] = [];
  serverWorkersMap: {[key: string]: Worker[]} = {};
  serverJobsMap: {[key: string]: JobDetail[]} = {};
  jobCommand = '';

  constructor(private cwmService: CwmService) {
    cwmService.servers().subscribe(servers => {
      this.servers = servers;
      this.selected = servers.map(s => false);

      this.servers.forEach((server, index, array) => {
        this.cwmService.workers(server.host, server.port)
        .subscribe(workers => {
          this.serverWorkersMap[this.serverHostPort(server)] = workers;
        });
        this.cwmService.jobs(server.host, server.port)
        .subscribe(jobs => {
          this.serverJobsMap[this.serverHostPort(server)] = jobs;
        });
      });
    });
  }

  ngOnInit() {
  }

  private serverHostPort(server: Server): string {
    return `${server.host}:${server.port}`;
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

  workerCount(server: Server): number {
    const hostPort = this.serverHostPort(server);
    const workers = this.serverWorkersMap[hostPort];
    return workers ? workers.length : 0;
  }

  jobCount(server: Server): number {
    const hostPort = this.serverHostPort(server);
    const jobs = this.serverJobsMap[hostPort];
    return jobs ? jobs.length : 0;
  }
}
