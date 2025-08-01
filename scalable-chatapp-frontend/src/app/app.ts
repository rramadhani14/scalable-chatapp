import { Component, signal } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { heroUserCircleSolid } from "@ng-icons/heroicons/solid";
@Component({
  selector: "app-root",
  imports: [RouterOutlet, NgIcon],
  providers: [provideIcons({ heroUserCircleSolid })],
  templateUrl: "./app.html",
  styleUrl: "./app.scss",
})
export class App {
  protected readonly title = signal("scalable-chatapp-frontend");
}
