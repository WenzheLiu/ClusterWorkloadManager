import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { APP_BASE_HREF } from '@angular/common';

import { AppComponent } from './app.component';
import { ServersComponent } from './ui/servers/servers.component';
import { AppRoutes } from './app.route';

import './utils/rxjs-operators';
import { CwmService } from './service/cwm.service';
import { ConfigService } from './service/config.service';
import { WorkersComponent } from './ui/workers/workers.component';
import { JobsComponent } from './ui/jobs/jobs.component';

@NgModule({
  declarations: [
    AppComponent,
    ServersComponent,
    WorkersComponent,
    JobsComponent
  ],
  imports: [
    BrowserModule, HttpModule, FormsModule,
    RouterModule.forRoot(AppRoutes)
  ],
  providers: [ConfigService, CwmService,
    { provide: APP_BASE_HREF, useValue: '/' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
