import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { ConfigService } from './config.service';
import { Server } from '../model/server';
import { Worker } from '../model/worker';
import { JobDetail } from '../model/job.detail';

@Injectable()
export class CwmService {

  private headers = new Headers({
    'Content-Type': 'application/json'
  });

  constructor(private http: Http, private config: ConfigService) { }

  servers(): Observable<Server[]> {
    return this.get('servers');
  }

  workers(host: string, port: string): Observable<Worker[]> {
    return this.get('workers', {
      host: host,
      port: port
    });
  }

  jobs(host: string, port: string): Observable<JobDetail[]> {
    return this.get('jobs', {
      host: host,
      port: port
    });
  }

  private get(action: string, params: any = {}): Observable<any> {
    return this.http.get(this.config.uri(action), {
      headers: this.headers,
      params: params
    })
    .map(res => res.json());
  }
}
