import { HttpClient, httpResource } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { LoginDto } from '../../shared/models/login';
import { environment } from '../../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);

  login(credential: LoginDto) {
    this.httpClient
      .post(environment.baseApiUrl + '/j_check_security', credential)
      .subscribe((it) => console.log(it));
  }
}
