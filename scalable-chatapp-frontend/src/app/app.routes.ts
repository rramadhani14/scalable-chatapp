import { Routes } from "@angular/router";
import { LoginPage } from "./auth/pages/login-page/login-page";
import { HomePage } from "./home/pages/home-page/home-page";
import { authGuard } from "./core/guards/auth-guard";

export const routes: Routes = [
  {
    path: "",
    component: HomePage,
    // canActivateChild: [authGuard],
    canActivate: [authGuard],
  },
  {
    path: "login",
    component: LoginPage,
  },
];
