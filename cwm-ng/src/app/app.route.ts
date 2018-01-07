import {Routes} from '@angular/router';
import { ServersComponent } from './servers/servers.component';

export const AppRoutes: Routes = [
  {path: '', redirectTo: 'servers', pathMatch: 'full'},
  {path: 'servers', component: ServersComponent},
];
