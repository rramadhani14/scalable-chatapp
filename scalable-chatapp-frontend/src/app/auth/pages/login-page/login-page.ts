import { Component, inject } from "@angular/core";
import { AuthService as AuthService } from "../../../core/services/auth-service";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { map, tap } from "rxjs";
import { LoginDto } from "../../../shared/models/login";
@Component({
  selector: "app-login-page",
  imports: [ReactiveFormsModule],
  templateUrl: "./login-page.html",
  styleUrl: "./login-page.scss",
})
export class LoginPage {
  authService = inject(AuthService);
  router = inject(Router);
  formBuilder = inject(FormBuilder);
  loginForm = this.formBuilder.group({
    username: this.formBuilder.control("", [Validators.required]),
    password: this.formBuilder.control("", [Validators.required]),
  });

  submit(event: Event) {
    event.preventDefault();
    this.authService
      .login(this.loginForm.value as LoginDto)
      .pipe(
        tap((it) =>
          this.authService
            .getCsrfToken()
            .subscribe((it) => console.log("CSRF loaded")),
        ),
      )
      .subscribe((t) => {
        console.log("Navigating to home");
      });
  }
}
