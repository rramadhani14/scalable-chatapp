import { Component, inject } from '@angular/core';
import { AuthService as AuthService } from '../../../core/services/auth-service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login-page',
  imports: [],
  templateUrl: './login-page.html',
  styleUrl: './login-page.scss',
})
export class LoginPage {
  authService = inject(AuthService);
  submit(event: Event) {
    event.preventDefault();
    this.authService.login({
      username: 'admin',
      password: 'admin123',
      csrf: '',
    });
  }
}
