import { Component, inject } from "@angular/core";
import { AuthService as AuthService } from "../../../core/services/auth-service";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { tap } from "rxjs";

@Component({
  selector: "app-login-page",
  imports: [],
  templateUrl: "./login-page.html",
  styleUrl: "./login-page.scss",
})
export class LoginPage {
  authService = inject(AuthService);
  router = inject(Router);
  submit(event: Event) {
    event.preventDefault();
    this.authService
      .login({
        username: "admin",
        password: "admin123",
        csrf: "",
      })
      // .pipe(tap(() => this.router.navigate(["/"], { replaceUrl: true })))
      .subscribe(() => {
        console.log("Navigating to home");
      });
  }
}
