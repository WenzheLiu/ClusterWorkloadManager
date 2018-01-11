import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CwmService } from '../../service/cwm.service';
import { Observable } from 'rxjs/Observable';
import { JobDetail } from '../../model/job.detail';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {

  host: string;
  port: string;
  jobDetails$: Observable<JobDetail[]>;

  constructor(private route: ActivatedRoute, private cwmService: CwmService) {
    route.params.subscribe(params => {
      this.host = params['host'];
      this.port = params['port'];
      this.jobDetails$ = cwmService.jobs(this.host, this.port);
    });
  }

  ngOnInit() {
  }

  jobClass(jobDetail: JobDetail): any {
    return {
      'done': jobDetail.status === 'DONE',
      'fail': jobDetail.status === 'FAIL'};
  }
}
