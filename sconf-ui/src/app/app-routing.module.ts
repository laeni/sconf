import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ClientComponent } from './client/client.component';
import { ClientGuard } from './client/client.guard';
import { CrisisDetailResolverService } from './client.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'client', component: ClientComponent,
    canActivate: [ClientGuard], canDeactivate: [ClientGuard],
    resolve: { clientInfo: CrisisDetailResolverService }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
