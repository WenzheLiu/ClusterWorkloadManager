import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { APP_BASE_HREF, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { AppComponent } from './app.component';
import { ServersComponent } from './servers/servers.component';
import { AppRoutes } from './app.route';

import './utils/rxjs-operators';
import { CwmService } from './service/cwm.service';
import { ConfigService } from './service/config.service';

@NgModule({
  declarations: [
    AppComponent,
    ServersComponent
  ],
  imports: [
    BrowserModule, HttpModule, FormsModule,
    RouterModule.forRoot(AppRoutes)
  ],
  providers: [ConfigService, CwmService,
    { provide: APP_BASE_HREF, useValue: '/' },
    // { provide: LocationStrategy, useClass: HashLocationStrategy } // for pre-HTML5
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
