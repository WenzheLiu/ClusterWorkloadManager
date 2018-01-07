import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { ConfigService } from './config.service';
import { Server } from '../model/server';

@Injectable()
export class CwmService {

  private headers = new Headers({
    'Content-Type': 'application/json'
  });

  constructor(private http: Http, private config: ConfigService) { }

  servers(): Observable<Server[]> {
    return this.http.get(this.config.uri('servers'), {headers: this.headers})
    .map(res => res.json());
  }

}
