import { HttpClient, httpResource } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { LoginDto } from "../../shared/models/login";
import { environment } from "../../../environments/environment";
import { catchError, map, Observable, of, tap } from "rxjs";
@Injectable({
  providedIn: "root",
})
export class AuthService {
  private httpClient = inject(HttpClient);

  login(credential: LoginDto) {
    console.log(credential);
    const loginForm = new FormData();
    loginForm.append("j_username", credential.username);
    loginForm.append("j_password", credential.password);
    return this.httpClient
      .post(environment.baseApiUrl + "/j_check_security", loginForm, {
        headers: {},
        withCredentials: true,
      })
      .pipe(tap((it) => console.log("Authentication succeed!")));
  }

  isAuthenticated(): Observable<boolean> {
    return this.httpClient
      .get(environment.baseApiUrl + "/api/v1/user/me", {
        withCredentials: true,
      })
      .pipe(
        catchError((err, caught) => of(false)),
        map((res) => (res === false ? false : true)),
      );
  }

  getCsrfToken(): Observable<null> {
    return this.httpClient.get<null>(
      environment.baseApiUrl + "/api/v1/auth/csrf",
      {
        withCredentials: true,
      },
    );
  }
}
