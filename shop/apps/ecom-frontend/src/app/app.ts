import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaConfig, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { Footer } from './layout/footer';
import { Navbar } from './layout/navbar';
import { fontAwesomeIcons } from './shared/font-awesome-icons';

@Component({
  imports: [RouterModule, Navbar, Footer],
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  private readonly iconLibrary = inject(FaIconLibrary);
  private readonly iconConfig = inject(FaConfig);

  protected title = 'ecom-frontend';

  ngOnInit(): void {
    this.iconConfig.defaultPrefix = 'far';
    this.iconLibrary.addIcons(...fontAwesomeIcons);
  }
}
